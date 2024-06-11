import {MdDelete, MdEdit} from "react-icons/md";
import {Link} from "react-router-dom";

export interface Book {
    id: number;
    authorId: number;
    name: string;
    publishDate: Date;
}

interface Props {
    books: Book[];
    previousLink: string;
    nextLink: string;
    hasPrevious: boolean;
    hasNext: boolean;
    handleEditBook: (book: Book) => void;
    handleDeleteBook: (book: Book) => void;
}

export default function BooksTable({
                                       books,
                                       previousLink,
                                       nextLink,
                                       hasPrevious,
                                       hasNext,
                                       handleEditBook,
                                       handleDeleteBook
                                   }: Props) {
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
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {books.map((book, index) => (
                    <tr key={book.id.toString()} className={index % 2 === 0 ? 'even-row' : 'odd-row'}>
                        <td>{book.id.toString()}</td>
                        <td>{book.authorId.toString()}</td>
                        <td>{book.name}</td>
                        <td>{formatDate(book.publishDate)}</td>
                        <td>
                            <button onClick={() => handleEditBook(book)}><MdEdit/></button>
                            <button onClick={() => handleDeleteBook(book)}><MdDelete/></button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <Link to={previousLink} className={!hasPrevious}>
                <button disabled={!hasPrevious}>Previous</button>
            </Link>
            <Link to={nextLink} className={!hasNext}>
                <button disabled={!hasNext}>Next</button>
            </Link>
        </>
    );
};
