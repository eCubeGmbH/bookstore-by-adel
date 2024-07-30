import {createBrowserRouter, RouterProvider} from "react-router-dom";
import ErrorPage from "./pages/ErrorPage.tsx";
import RootLayout from "./components/RootLayout.tsx";
import AuthorsListPage from "./pages/AuthorsListPage.tsx";
import HomePage from "./pages/HomePage.tsx";
import BooksListPage from "./pages/BooksListPage.tsx";
import BookFormPage from "./pages/BookFormPage.tsx";
import BookDetailsPage from "./pages/BookDetailsPage.tsx";
import AuthorDetailsPage from "./pages/AuthorDetailsPage.tsx";
import AuthorFormPage from "./pages/AuthorFormPage.tsx";

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
                        {
                            path: 'new',
                            element: <AuthorFormPage/>
                        },
                        {
                            path: ':id',
                            element: <AuthorDetailsPage/>,
                            loader: AuthorDetailsPage.loader
                        },

                    ]
                },
                {
                    path: 'books',
                    children: [
                        {
                            index: true,
                            element: <BooksListPage/>,
                            loader: BooksListPage.loader
                        },
                        {
                            path: 'new',
                            element: <BookFormPage/>,
                        },
                        {
                            path: ':id',
                            element: <BookDetailsPage/>,
                            loader: BookDetailsPage.loader
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
