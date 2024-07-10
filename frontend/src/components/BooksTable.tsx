import {MdEdit} from "react-icons/md";
import {Link, useLoaderData, useSearchParams} from "react-router-dom";
import '../assets/booksDisplay.css';
import {SortField, SortOrder} from "../pages/BooksListPage.tsx";

export interface Book {
    id: number;
    authorId: number;
    name: string;
    publishDate: Date;
    authorName?: string;
}

interface Props {
    books: Book[],
    previousLink: () => void;
    nextLink: () => void;
    hasPrevious: boolean;
    hasNext: boolean;
    sortField:string
    sortOrder: string;
    handleEditBook: (book: Book) => void;
    handleViewBookDetails?: (book: Book) => void;

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
    const [searchParams, setSearchParams] = useSearchParams();
    const sortField: SortField = SortField[searchParams.get(`sortField`) as keyof typeof SortField] || SortField.NAME;
    const sortOrder: SortOrder = SortOrder[searchParams.get(`sortOrder`) as keyof typeof SortOrder] || SortOrder.ASC;

    const formatDate = (date: Date): string => {
        return new Date(date).toLocaleDateString('de-DE', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    };
    const handleSort = (sortField: SortField, sortOrder: SortOrder) => {
        setSearchParams({
            ...Object.fromEntries(searchParams.entries()),
            sortField: SortField[sortField],
            sortOrder: SortOrder[sortOrder],
            pageNumber: '0',
        });
    };

    return (
        <>
            <table className="books-table">
                <thead>
                <tr>
                    <th onClick={() => handleSort(SortField.ID, sortOrder === SortOrder.ASC ? SortOrder.DESC : SortOrder.ASC)}
                        title={"click to sort by Id"}>Book
                        ID {sortField !== SortField.ID ? '' : sortOrder === SortOrder.ASC ? '▲' : '▼'}</th>
                    <th onClick={() => handleSort(SortField.AUTHORID, sortOrder === SortOrder.ASC ? SortOrder.DESC : SortOrder.ASC)}
                        title={"click to sort by AuthorId"}>Author
                        ID {sortField === SortField.AUTHORID ? (sortOrder === SortOrder.ASC ? '▲' : '▼') : ''}</th>
                    <th onClick={() => handleSort(SortField.NAME, sortOrder === SortOrder.ASC ? SortOrder.DESC : SortOrder.ASC)}
                        title={"click to sort by Name"}>Name {sortField === SortField.NAME ? (sortOrder === SortOrder.ASC ? '▲' : '▼') : ''}</th>
                    <th onClick={() => handleSort(SortField.PUBLISHDATE, sortOrder === SortOrder.ASC ? SortOrder.DESC : SortOrder.ASC)}
                        title={"click to sort by PublishDate"}>PublishDate {sortField === SortField.PUBLISHDATE ? (sortOrder === SortOrder.ASC ? '▲' : '▼') : ''}</th>
                    <th onClick={() => handleSort(SortField.AUTHOR, sortOrder === SortOrder.ASC ? SortOrder.DESC : SortOrder.ASC)}
                        title={"click to sort by Author"}>Author {sortField === SortField.AUTHOR ? (sortOrder === SortOrder.ASC ? '▲' : '▼') : ''}</th>
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