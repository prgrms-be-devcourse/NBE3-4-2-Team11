import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { getAccessToken, getRefreshToken,  setTokens, removeTokens } from "../utils/token"; // ✅ 토큰 유틸 함수 가져오기

type AuthState = {
    isLoggedIn: boolean;
    accessToken: string | null;
    refreshToken: string | null;
    login: (accessToken: string, refreshToken: string) => void;
    logout: () => void;
    refreshAccessToken: () => Promise<boolean>;
};

export const useAuthStore = create<AuthState>()(
    persist(
        (set, get) => ({
            isLoggedIn: false,
            accessToken: getAccessToken(),
            refreshToken: getRefreshToken(),

            login: (accessToken, refreshToken) => {
                if (!refreshToken) {
                    console.warn("❌ WARNING: refreshToken이 없습니다. 백엔드 응답 확인 필요!");
                }

                setTokens(accessToken,refreshToken); // ✅ 유틸 함수 사용

                const payload: any = JSON.parse(atob(accessToken.split(".")[1]));

                if (!payload.exp)  {
                    console.error("❌ JWT payload에 exp 필드가 없습니다. 토큰을 확인하세요:", payload);
                }

                set({ isLoggedIn: true, accessToken, refreshToken });
            },

            logout: async () => {
                if (typeof window === "undefined") return;

                removeTokens(); // ✅ 유틸 함수 사용
                set({ isLoggedIn: false, accessToken: null, refreshToken: null });
            },

            refreshAccessToken: async () => {
                const refreshToken = getRefreshToken();
                if (!refreshToken) {
                    console.warn("❌ Refresh Token 없음, 로그아웃 진행 필요");
                    get().logout();
                    return false;
                }

                try {
                    const response = await fetch("/api/v1/token/refresh", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify({ refreshToken }),
                    });

                    if (!response.ok) {
                        console.warn("❌ Refresh Token이 만료됨, 로그아웃 진행");
                        get().logout();
                        return false;
                    }

                    const data = await response.json();
                    console.log("✅ Access Token 갱신 완료:", data.accessToken);

                    setTokens(data.data.accessToken,data.data.refreshToken); // ✅ 유틸 함수 사용
                    set({ accessToken: data.data.accessToken });

                    return true;
                } catch (error) {
                    console.error("❌ Access Token 갱신 실패:", error);
                    get().logout();
                    return false;
                }
            },
        }),
        {
            name: "auth-storage",
            storage: typeof window !== "undefined"
                ? createJSONStorage(() => localStorage)
                : undefined,
        }
    )
);
