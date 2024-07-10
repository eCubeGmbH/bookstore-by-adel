import {LoaderFunction, useLoaderData, useNavigate, useSearchParams} from "react-router-dom";
import BooksTable, {Book} from "../components/BooksTable";
import {useState} from "react";

export enum SortOrder {
    ASC = 'ASC', DESC = 'DESC'
}

export enum SortField {
    ID = 'ID', AUTHORID = 'AUTHORID', NAME = 'NAME', PUBLISHDATE = 'PUBLISHDATE', AUTHOR = 'AUTHOR'
}

const loader: LoaderFunction = async function getData({request}) {
    const url = new URL(request.url);
    const pageNumber = +(url.searchParams.get("pageNumber") || 0);
    const pageSize = +(url.searchParams.get("pageSize") || 10);
    const sortField = url.searchParams.get("sortField") || "NAME";
    const sortOrder = url.searchParams.get("sortOrder") || "ASC";
    const response = await fetch(`/api/books?pageNumber=${pageNumber}&pageSize=${pageSize}&sortField=${sortField}&sortOrder=${sortOrder}`);
    return response.json();
}
const BooksListPage = () => {
    const loaderData = useLoaderData() as Book[];
    const [books] = useState(loaderData);
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const pageNumber = +(searchParams.get("pageNumber") || 0);
    const pageSize = +(searchParams.get("pageSize") || 10);
    const sortField = searchParams.get("sortField") || "NAME";
    const sortOrder = searchParams.get("sortOrder") || "ASC";


    const handleEditBook = (book: Book) => {
        navigate(`/books/${book.id}`);
    };

    const handleViewBookDetails = (book: Book) => {
        navigate(`/books/${book.id}`);
    };

    const handleCreateBook = () => {
        navigate('/books/new');
    };

    const handlePageChange = (direction: "next" | "prev") => {
        const newPageNumber = direction === "next" ? pageNumber + 1 : pageNumber - 1;
        navigate(`/books?pageNumber=${newPageNumber}&pageSize=${pageSize}&sortField=${sortField}&sortOrder=${sortOrder}`);
    };
    return (
        <div>
            <button onClick={handleCreateBook}>Add New Book</button>
            <BooksTable
                books={books}
                handleEditBook={handleEditBook}
                handleViewBookDetails={handleViewBookDetails}
                sortField={sortField}
                sortOrder={sortOrder}
                hasNext={books.length === pageSize}
                hasPrevious={pageNumber > 0}
                nextLink={() => handlePageChange("next")}
                previousLink={() => handlePageChange("prev")}
            />
        </div>
    );
};
BooksListPage.loader = loader;
export default BooksListPage;