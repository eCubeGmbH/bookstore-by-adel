import '../assets/index.css'
import '../assets/sort-symbols.css'
import {MdDelete, MdEdit} from "react-icons/md";
import {Link, useLoaderData, useSearchParams} from "react-router-dom";
import {SortField, SortOrder} from "../pages/AuthorsListPage.tsx";

export interface Author {
    id: number;
    name: string;
    country: string;
    birthDate: Date;
}

interface Props {
    authors: Author[];
    previousLink: string;
    nextLink: string;
    hasPrevious: boolean;
    hasNext: boolean;
    sortField: SortField;
    sortOrder: SortOrder;
    handleEditAuthor: (author: Author) => void;
    handleDeleteAuthor: (author: Author) => void;
}

export interface AuthorsEnvelopDto {
    pageNumber: number;
    pageSize: number;
    authorsCount: number;
    sortField: string;
    sortOrder: string;
    authorName: string;
    authors: Author[];
}

export default function AuthorsTable({

                                         previousLink,
                                         nextLink,
                                         hasPrevious,
                                         hasNext,
                                         handleEditAuthor,
                                         handleDeleteAuthor
                                     }: Props) {
    const loaderAuthors = useLoaderData() as AuthorsEnvelopDto;
    const [searchParams, setSearchParams] = useSearchParams();
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
            sortField: sortField.toString(),
            sortOrder: sortOrder.toString(),
        });
    };
    return (
        <>
            <table className="authors-table">
                <thead>
                <tr>
                    <th onClick={() => handleSort(SortField.ID, SortOrder.ASC)}
                        title={"click to sort by Id"}>Author ID {SortField.ID ? (SortOrder.ASC ? '▲' : '▼') : ''}</th>
                    <th onClick={() => handleSort(SortField.NAME, SortOrder.ASC)}
                        title={"click to sort by Name"}>Name {SortField.NAME ? (SortOrder.ASC ? '▲' : '▼') : ''}</th>
                    <th onClick={() => handleSort(SortField.COUNTRY, SortOrder.ASC)}
                        title={"Sort nach Country"}>Country {SortField.COUNTRY ? (SortOrder.ASC ? '▲' : '▼') : ''}</th>
                    <th onClick={() => handleSort(SortField.BIRTHDATE, SortOrder.ASC)}
                        title={"click to sort by Birthdate"}>BirthDate {SortField.BIRTHDATE ? (SortOrder.ASC ? '▲' : '▼') : ''}</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {loaderAuthors.authors.map((author, index) => (
                    <tr key={author.id.toString()} className={index % 2 === 0 ? 'even-row' : 'odd-row'}>
                        <td>{author.id.toString()}</td>
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
            <Link to={previousLink} disabled={!hasPrevious}>
                <button disabled={!hasPrevious}>Previous</button>
            </Link>
            <Link to={nextLink} disabled={!hasNext}>
                <button disabled={!hasNext}>Next</button>
            </Link>
        </>
    );
};

