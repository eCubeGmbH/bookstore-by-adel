import {MdEdit} from "react-icons/md";
import {Link, useLoaderData} from "react-router-dom";
import '../assets/booksDisplay.css';
import {SortField, SortOrder} from "../pages/AuthorsListPage.tsx";

export interface Book {
    id: number;
    authorId: number;
    name: string;
    publishDate: Date;
    authorName?: string;
}

interface Props {
    books: Book[],
    previousLink: () => void,
    nextLink: () => void,
    hasPrevious: boolean,
    hasNext: boolean,
    handleEditBook: (book: Book) => void,
    handleViewBookDetails?: (book: Book) => void,
    handleSortChange?: (newSortField: SortField, newSortOrder: SortOrder) => void
}

export interface BooksEnvelopDto {
    pageNumber: number;
    pageSize: number;
    booksCount: number;
    sortField: string;
    sortOrder: string;
    bookName: string;
    bookList: Book[];
}

export default function BooksTable({
                                       previousLink,
                                       nextLink,
                                       hasPrevious,
                                       hasNext,
                                       handleEditBook,
                                   }: Props) {
    const loaderBooks = useLoaderData() as BooksEnvelopDto;

    const formatDate = (date: Date): string => {
        return new Date(date).toLocaleDateString('de-DE', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    };

    return (
        <>
            <table className="books-table">
                <thead>
                <tr>
                    <th>Book ID</th>
                    <th>Author Id</th>
                    <th>Name</th>
                    <th>Publish Date</th>
                    <th>Author</th>
                    <th>Edit</th>
                </tr>
                </thead>
                <tbody>
                {loaderBooks.bookList.map((book, index) => (
                    <tr key={book.id.toString()} className={index % 2 === 0 ? 'even-row' : 'odd-row'}>
                        <td>{book.id.toString()}</td>
                        <td>{book.authorId.toString()}</td>
                        <td>{book.name}</td>
                        <td>{formatDate(book.publishDate)}</td>
                        <td>
                            <Link to={`/authors/${book.authorName}`}> {book.authorName}</Link>
                        </td>
                        <td>
                            <button onClick={() => handleEditBook(book)}><MdEdit/></button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <Link to={previousLink} className={hasPrevious ? '' : 'disabled-link'}>
                <button disabled={!hasPrevious}>Previous</button>
            </Link>
            <Link to={nextLink} className={hasNext ? '' : 'disabled-link'}>
                <button disabled={!hasNext}>Next</button>
            </Link>
        </>
    );
};