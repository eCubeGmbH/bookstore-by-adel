import {LoaderFunction, useLoaderData, useNavigate, useSearchParams} from "react-router-dom";
import BooksTable, {Book, BooksEnvelopDto} from "../components/BooksTable";
import "../assets/booksDisplay.css"

export enum SortOrder {
    ASC = 'ASC', DESC = 'DESC'
}

export enum SortField {
    ID = 'ID', AUTHOR_ID = 'AUTHOR_ID', NAME = 'NAME', PUBLISH_DATE = 'PUBLISH_DATE', AUTHOR = 'AUTHOR'
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
    const bookData = useLoaderData() as BooksEnvelopDto;
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const pageNumber = +(searchParams.get("pageNumber") || 0);
    const pageSize = +(searchParams.get("pageSize") || 10);
    const sortField = searchParams.get("sortField") || "NAME";
    const sortOrder = searchParams.get("sortOrder") || "ASC";
    const hasNext = bookData.bookList.length === pageSize;


    const handleEditBook = (book: Book) => {
        navigate(`/books/${book.id}`);
    };

    const handleViewBookDetails = (book: Book) => {
        navigate(`/books/${book.id}`);
    };

    const handleCreateBook = () => {
        navigate('/books/new');
    };
    const hasPrevious = pageNumber > 0;
    const previousPage = hasPrevious ? pageNumber - 1 : 0;
    const nextPage = hasNext ? pageNumber + 1 : pageNumber;
    const nextLink: string = `?pageNumber=${nextPage}&pageSize=${pageSize}&sortField=${sortField}&sortOrder=${sortOrder}`;
    const previousLink: string = `?pageNumber=${previousPage}&pageSize=${pageSize}&sortField=${sortField}&sortOrder=${sortOrder}`;
    return (
        <div>
            <button className={"create-book-button"} onClick={handleCreateBook}>Add New Book</button>
            <BooksTable
                books={bookData.bookList}
                handleEditBook={handleEditBook}
                handleViewBookDetails={handleViewBookDetails}
                sortField={sortField}
                sortOrder={sortOrder}
                hasNext={hasNext}
                hasPrevious={pageNumber !== 0}
                nextLink={nextLink}
                previousLink={previousLink}
            />
        </div>
    );
};
BooksListPage.loader = loader;
export default BooksListPage;