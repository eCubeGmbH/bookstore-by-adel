import '../assets/authorsDisplay.css'
import {LoaderFunction, useLoaderData, useNavigate, useSearchParams} from "react-router-dom";
import AuthorsTable, {Author, AuthorsEnvelopDto} from '../components/AuthorsTable.tsx';


export enum SortOrder {
    ASC = 'ASC', DESC = 'DESC'
}

export enum SortField {
    ID = 'ID', NAME = 'NAME', COUNTRY = 'COUNTRY', BIRTHDATE = 'BIRTHDATE'
}

const loader: LoaderFunction = async function getData({request}) {
    const url = new URL(request.url);
    const pageNumber = +(url.searchParams.get("pageNumber") || 0);
    const pageSize = +(url.searchParams.get("pageSize") || 10);
    const sortField = url.searchParams.get("sortField") || "NAME";
    const sortOrder = url.searchParams.get("sortOrder") || "ASC";
    const response = await fetch(`/api/authors?pageNumber=${pageNumber}&pageSize=${pageSize}&sortField=${sortField}&sortOrder=${sortOrder}`);
    return response.json();
}

const AuthorsListPage = () => {
    const authorData = useLoaderData() as AuthorsEnvelopDto;
    const navigate = useNavigate();
    const [search] = useSearchParams();
    const pageSize = +(search.get(`pageSize`) || 10);
    const pageNumber = +(search.get(`pageNumber`) || 0);
    const sortField: SortField = (search.get(`sortField`) || 'NAME') as SortField;
    const sortOrder: SortOrder = (search.get(`sortOrder`) || 'ASC') as SortOrder;
    const hasNext = authorData.authors.length === pageSize;


    const handleCreateAuthor = () => {
        navigate('/authors/new');
    };
    const handleEditAuthor = (author: Author) => {
        navigate(`/authors/${author.id}`);
    };
    const previousPage: number = pageNumber === 0 ? 0 : pageNumber - 1;
    const nextPage: number = pageNumber + 1;
    const nextLink: string = `?pageNumber=${nextPage}&pageSize=${pageSize}&sortField=${sortField}&sortOrder=${sortOrder}`;
    const previousLink: string = `?pageNumber=${previousPage}&pageSize=${pageSize}&sortField=${sortField}&sortOrder=${sortOrder}`;

    //JSX
    return (
        <>
            <button className="create-author-button" onClick={handleCreateAuthor}>Create Author</button>
            <AuthorsTable
                authors={authorData.authors}
                nextLink={nextLink}
                previousLink={previousLink}
                hasPrevious={pageNumber !== 0}
                hasNext={hasNext}
                handleEditAuthor={handleEditAuthor}
                sortField={sortField}
                sortOrder={sortOrder}
            />
        </>
    );
}

AuthorsListPage.loader = loader;

export default AuthorsListPage;