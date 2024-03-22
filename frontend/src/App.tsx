import './assets/index.css'
import 'react-toastify/dist/ReactToastify.css';
import {useEffect, useState} from 'react';
import AuthorsTable, {Author} from './AuthorsTable.tsx';
import Header from './Header.tsx';
import ModifyAuthor from './ModifyAuthor.tsx';
import {ToastContainer} from "react-toastify";
import {errorNotify, successNotify} from "./Notifications.ts";

const MyMainComponent = () => {
        // Constants and state declarations
        const pageSize = 10;
        const [isVisible, setIsVisible] = useState(false);
        const [authorData, setAuthorData] = useState<Author[]>([]);
        const [currentPage, setCurrentPage] = useState<number>(0);
        const [hasNext, setHasNext] = useState<boolean>(true);
        const [currentAuthor, setCurrentAuthor] = useState<Author | null>(null);
        const [mode, setMode] = useState<'add' | 'modify' | 'delete'>('add');

        async function fetchAuthors(pageNumber: number): Promise<Author[]> {
            // Function to fetch authors data from the server (GET)
            const from: number = pageNumber * pageSize;
            const to: number = (pageNumber + 1) * pageSize;
            console.log(`fetchAuthors: pageNumber: ${pageNumber} - from:${from}, to:${to}`)
            const response = await fetch(`/api/authors?from=${from}&to=${to}`);
            if (!response.ok) {
                throw new Error(response.statusText);
            }
            return response.json();
        }

        // Handlers for CRUD operations on authors
        const handleOnClickAddAuthor = async (author: Author) => {
            fetch('/api/authors', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(author)
            }).then(() => {
                updateCurrentPage(currentPage);
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
                updateCurrentPage(currentPage);
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
                updateCurrentPage(currentPage);
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

        const updateCurrentPage = (pageNumber: number): void => {
            fetchAuthors(pageNumber)
                .then(authors => {
                    setAuthorData(previousAuthor => previousAuthor.filter(() => false));
                    if (authors.length === pageSize) {
                        setAuthorData(() => authors.slice(0, pageSize));
                        setHasNext(() => true);
                    } else {
                        setAuthorData(() => authors);
                        setHasNext(() => false);
                    }
                })
            setCurrentPage(() => pageNumber);
        };

        useEffect(() => {
                updateCurrentPage(0);
            }, []
        )

        const previousPage: number = currentPage === 0 ? 0 : currentPage - 1;
        const nextPage: number = currentPage + 1;
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
        //JSX
        return (
            <>
                <ToastContainer/>
                <button className="create-author-button" onClick={handleIsVisible}>Create Author</button>
                {isVisible && ((
                    <ModifyAuthor author={currentAuthor} onClickCancel={handleUnVisible} title={title}
                                  buttonLabel={title} buttonHandler={buttonHandler}/>))}
                <Header title="Authors"/>
                <AuthorsTable authors={authorData} nextPage={nextPage} prevPage={previousPage}
                              onUpdateCurrentPage={updateCurrentPage}
                              hasPrevious={currentPage !== 0} hasNext={hasNext} handleEditAuthor={handleEdit}
                              handleDeleteAuthor={handleDelete}
                />
            </>
        );
    }
;

export default MyMainComponent;

