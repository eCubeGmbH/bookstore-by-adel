import { LoaderFunction, useLoaderData, useNavigate } from "react-router-dom";
import BooksTable, { Book } from "../components/BooksTable";
import { useState } from "react";

export const loader: LoaderFunction = async function getData({ request }) {
    const url = new URL(request.url);
    const response = await fetch(url);
    return response.json();
}

const BooksListPage = () => {
    const loaderData = useLoaderData() as Book[];
    const [books, setBooks] = useState(loaderData);
    const navigate = useNavigate();

    const handleDeleteBook = async (book: Book) => {
        await fetch(`/api/books/${book.id}`, {
            method: 'DELETE'
        });
        setBooks(books.filter(b => b.id !== book.id));
    };

    const handleEditBook = (book: Book) => {
        navigate(`/books/${book.id}`);
    };

    const handleCreateBook = () => {
        navigate('/books/new');
    };

    const previousLink = "/books?page=prev";
    const nextLink = "/books?page=next";
    const hasPrevious = true;
    const hasNext = true;

    return (
        <div>
            <h1>Books</h1>
            <button onClick={handleCreateBook}>Add New Book</button>
            <BooksTable
                books={books}
                handleDeleteBook={handleDeleteBook}
                handleEditBook={handleEditBook}
                hasNext={hasNext}
                hasPrevious={hasPrevious}
                nextLink={nextLink}
                previousLink={previousLink}
            />
        </div>
    );
};
BooksListPage.loader = loader;
export default BooksListPage;
