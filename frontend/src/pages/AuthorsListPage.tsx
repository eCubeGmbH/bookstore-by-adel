import '../assets/authorsDisplay.css'
import 'react-toastify/dist/ReactToastify.css';
import {useState} from "react";
import {LoaderFunction, useLoaderData, useSearchParams} from "react-router-dom";
import AuthorsTable, {Author, AuthorsEnvelopDto} from '../components/AuthorsTable.tsx';
import ModifyAuthor from '../components/ModifyAuthor.tsx';
import {errorNotify, successNotify} from "../components/Notifications.ts";

export enum SortOrder {
    ASC='ASC', DESC='DESC'
}
export enum SortField {
     ID= 'ID', NAME='NAME', COUNTRY='COUNTRY', BIRTHDATE='BIRTHDATE'
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
    const [search] = useSearchParams();
    const pageSize = +(search.get(`pageSize`) || 10);
    const pageNumber = +(search.get(`pageNumber`) || 0);
    const sortField: SortField = (search.get(`sortField`) || 'NAME') as SortField;
    const sortOrder: SortOrder = (search.get(`sortOrder`) || 'ASC') as SortOrder;
    const hasNext = authorData.authors.length === pageSize;

    // Constants and state declarations
    const [isVisible, setIsVisible] = useState(false);
    const [currentAuthor, setCurrentAuthor] = useState<Author | null>(null);
    const [mode, setMode] = useState<'add' | 'modify' | 'delete'>('add');

    // Handlers for CRUD operations on authors
    const handleOnClickAddAuthor = async (author: Author) => {
        fetch('/api/authors', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(author)
        }).then(() => {
            successNotify({autoClose: 3000, message: "Succeed !"});
            setIsVisible(false);
        }).catch(() => errorNotify({autoClose: 3000, message: "Failed"}));
    }

    const handleOnClickUpdateAuthor = async (author: Author) => {
        fetch(`/api/authors/${author.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(author)
        }).then(() => {
            successNotify({autoClose: 3000, message: "Edit Succeed !"})
            setIsVisible(false);
        }).catch(() => errorNotify({autoClose: 3000, message: "Edit Failed !"}))
    }

    const handleOnClickDeleteAuthor = async (author: Author) => {
        fetch(`/api/authors/${author.id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        }).then(() => {
            successNotify({autoClose: 3000, message: "Delete Succeed !"})
            setIsVisible(false);
        }).catch(() => errorNotify({autoClose: 3000, message: "Delete Failed !"}))
    }
    // Handlers for UI actions
    const handleEdit = (author: Author) => {
        setCurrentAuthor(author);
        setIsVisible(true);
        setMode("modify");
    }

    const handleDelete = (author: Author) => {
        setCurrentAuthor(author);
        setIsVisible(true);
        setMode("delete");
    }

    const handleIsVisible = () => {
        setCurrentAuthor(null)
        setIsVisible(true);
        setMode("add");
    };

    const handleUnVisible = () => {
        setIsVisible(false);
    };

    // Title and button handler based on the mode

    let title: string;
    let buttonHandler: (author: Author) => void;
    if (mode === 'add') {
        title = 'Add new author';
        buttonHandler = handleOnClickAddAuthor;
    } else if (mode === "modify") {
        title = 'Edit author';
        buttonHandler = handleOnClickUpdateAuthor;
    } else {
        title = 'Delete author?';
        buttonHandler = handleOnClickDeleteAuthor;
    }

    const previousPage: number = pageNumber === 0 ? 0 : pageNumber - 1;
    const nextPage: number = pageNumber + 1;
    const nextLink: string = `?pageNumber=${nextPage}&pageSize=${pageSize}&sortField=${sortField}&sortOrder=${sortOrder}`;
    const previousLink: string = `?pageNumber=${previousPage}&pageSize=${pageSize}&sortField=${sortField}&sortOrder=${sortOrder}`;

    //JSX
    return (
        <>
            <button className="create-author-button" onClick={handleIsVisible}>Create Author</button>
            {isVisible && ((
                <ModifyAuthor author={currentAuthor} onClickCancel={handleUnVisible} title={title}
                              buttonLabel={title} buttonHandler={buttonHandler}/>))}
            <AuthorsTable authors={authorData.authors} nextLink={nextLink} previousLink={previousLink}
                          hasPrevious={pageNumber !== 0} hasNext={hasNext} handleEditAuthor={handleEdit}
                          handleDeleteAuthor={handleDelete} sortField={sortField} sortOrder={sortOrder}
            />
        </>
    );
}

AuthorsListPage.loader = loader;

export default AuthorsListPage;