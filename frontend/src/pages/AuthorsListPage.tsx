import '../assets/index.css'
import 'react-toastify/dist/ReactToastify.css';
import AuthorsTable, {Author} from '../components/AuthorsTable.tsx';
import Header from '../components/Header.tsx';
import ModifyAuthor from '../components/ModifyAuthor.tsx';
import {ToastContainer} from "react-toastify";
import {errorNotify, successNotify} from "../components/Notifications.ts";
import {LoaderFunction, useLoaderData, useSearchParams} from "react-router-dom";
import {useState} from "react";


const loader: LoaderFunction = async function getData({request}) {
    const url = new URL(request.url);
    const pageNumber = +(url.searchParams.get("pageNumber") || 0);
    const pageSize = +(url.searchParams.get("pageSize") || 10);
    const from = pageNumber * pageSize;
    const to = (pageNumber + 1) * pageSize;

    console.log(`pageNumber: ${pageNumber}; pageSize: ${pageSize}`)

    const response = await fetch(`/api/authors?from=${from}&to=${to}`);

    return response.json();
}

const AuthorsListPage = () => {
        const authorData = useLoaderData() as Author[];
        const [search] = useSearchParams();
        console.log(`pageSize ${search.get(`pageSize`)}, pageNumber ${search.get(`pageNumber`)}`);
        const pageSize = +(search.get(`pageSize`) || 10);
        const pageNumber = +(search.get(`pageNumber`) || 0);
        const hasNext = authorData.length === pageSize;

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

        const previousPage: number = pageNumber === 0 ? 0 : pageNumber - 1;
        const nextPage: number = pageNumber + 1;

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

        const nextLink: string = `?pageNumber=${nextPage}&pageSize=${pageSize}`;
        const previousLink: string = `?pageNumber=${previousPage}&pageSize=${pageSize}`;

        //JSX
        return (
            <>
                <ToastContainer/>
                <button className="create-author-button" onClick={handleIsVisible}>Create Author</button>
                {isVisible && ((
                    <ModifyAuthor author={currentAuthor} onClickCancel={handleUnVisible} title={title}
                                  buttonLabel={title} buttonHandler={buttonHandler}/>))}
                <Header title="Authors"/>
                <AuthorsTable authors={authorData} nextLink={nextLink} previousLink={previousLink}
                              hasPrevious={pageNumber !== 0} hasNext={hasNext} handleEditAuthor={handleEdit}
                              handleDeleteAuthor={handleDelete}
                />
            </>
        );
    }
;

AuthorsListPage.loader = loader;

export default AuthorsListPage;


