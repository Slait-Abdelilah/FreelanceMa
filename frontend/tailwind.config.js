export default {
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            colors: {
                // palette Upwork
                brand: {
                    50: '#E4F7E4',
                    100: '#C5EDC5',
                    500: '#14A800',
                    600: '#108A00',
                    700: '#0D6E00',
                    900: '#001E00',
                },
                ink: {
                    DEFAULT: '#001E00',
                    soft: '#5E6D55',
                },
                cream: '#F7FAF4',
            },
            fontFamily: {
                sans: ['Inter', 'sans-serif'],
            },
            animation: {
                'fade-up': 'fadeUp 0.6s ease-out both',
                'fade-in': 'fadeIn 0.8s ease-out both',
            },
            keyframes: {
                fadeUp: {
                    '0%': { opacity: '0', transform: 'translateY(20px)' },
                    '100%': { opacity: '1', transform: 'translateY(0)' },
                },
                fadeIn: {
                    '0%': { opacity: '0' },
                    '100%': { opacity: '1' },
                },
            }
        },
    },
    plugins: [],
}