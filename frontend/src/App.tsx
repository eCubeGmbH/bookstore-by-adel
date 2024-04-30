import {createBrowserRouter, RouterProvider} from "react-router-dom";
import ErrorPage from "./pages/ErrorPage.tsx";
import RootLayout from "./components/RootLayout.tsx";
import AuthorsListPage from "./pages/AuthorsListPage.tsx";
import HomePage from "./pages/HomePage.tsx";

export default function App() {

    const router = createBrowserRouter([
        {
            path: '/',
            errorElement: <ErrorPage/>,
            element: <RootLayout/>,
            children: [
                {
                    index: true,
                    element: <HomePage/>
                },
                {
                    path: 'authors',
                    children: [
                        {
                            index: true,
                            element: <AuthorsListPage/>,
                            loader: AuthorsListPage.loader
                        },

                    ]
                },

            ]
        },
    ]);
    return (
        <>
            <RouterProvider router={router}/>

        </>
    );
}

