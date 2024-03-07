import './App.css'
import {useState} from "react";

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
}

export default function AuthorsTable({authors, prevPage, nextPage}: Props) {
    const [currentPage, setCurrentPage] = useState<number>(1);
    // const indexOfLastAuthor = currentPage * authorsPerPage;
    // const indexOfFirstAuthor = indexOfLastAuthor - authorsPerPage;
    // const currentAuthors = authors.slice(indexOfFirstAuthor, indexOfLastAuthor);

    const onClickHandler = (pageNumber: number): void => {
        if (currentPage > 1) {
            setCurrentPage(pageNumber - 1);
        }
    };

    return (
        <div>
            <table className="authors-table">
                <thead>
                <tr>
                    <th>Author ID</th>
                    <th>Name</th>
                    <th>Country</th>
                    <th>Birth Date</th>
                </tr>
                </thead>
                <tbody>
                {authors.map((author, index) => (
                    <tr key={author.id} className={index % 2 === 0 ? 'even-row' : 'odd-row'}>
                        <td>{author.id}</td>
                        <td>{author.name}</td>
                        <td>{author.country}</td>
                        <td>{`${author.birthDate}`}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <button onClick={() => onClickHandler(prevPage)}>Previous</button>
            <button onClick={() => onClickHandler(nextPage)}>Next</button>
        </div>
    );
};

