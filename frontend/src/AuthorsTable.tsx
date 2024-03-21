import './assets/index.css'
import {MdDelete, MdEdit} from "react-icons/md";

export interface Author {
    id: string;
    name: string;
    country: string;
    birthDate: Date;
}

interface Props {
    authors: Author[];
    prevPage: number;
    nextPage: number;
    onUpdateCurrentPage: (pageNumber: number) => void;
    hasPrevious: boolean;
    hasNext: boolean;
    handleEditAuthor: (author: Author) => void;
    handleDeleteAuthor: (author: Author) => void;
}

export default function AuthorsTable({
                                         authors,
                                         prevPage,
                                         nextPage,
                                         onUpdateCurrentPage,
                                         hasPrevious,
                                         hasNext,
                                         handleEditAuthor,
                                         handleDeleteAuthor
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
            <table className="authors-table">
                <thead>
                <tr>
                    <th>Author ID</th>
                    <th>Name</th>
                    <th>Country</th>
                    <th>Birth Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {authors.map((author, index) => (
                    <tr key={author.id} className={index % 2 === 0 ? 'even-row' : 'odd-row'}>
                        <td>{author.id}</td>
                        <td>{author.name}</td>
                        <td>{author.country}</td>
                        <td>{formatDate(author.birthDate)}</td>
                        <td>
                            <button onClick={() => handleEditAuthor(author)}><MdEdit/></button>
                            <button onClick={() => handleDeleteAuthor(author)}><MdDelete/></button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <button onClick={() => onUpdateCurrentPage(prevPage)} disabled={!hasPrevious}>Previous</button>
            <button onClick={() => onUpdateCurrentPage(nextPage)} disabled={!hasNext}>Next</button>
        </>
    );
};

