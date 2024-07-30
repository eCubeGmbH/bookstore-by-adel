import {LoaderFunction, useLoaderData, useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import "../assets/edit-book-author.css"

interface Book {
    id: number;
    authorId: number;
    name: string;
    publishDate: string;
    authorName: string;
}

export const loader: LoaderFunction = async ({params}) => {
    if (params.id) {
        const response = await fetch(`/api/books/${params.id}`);
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Failed to load book data");
        }
    }
    return null;
};

const BookDetailsPage = () => {
    const navigate = useNavigate();
    const {id} = useParams();
    const loaderData = useLoaderData() as Book;
    const [book, setBook] = useState<Book>(
        loaderData || {
            id: 0,
            authorId: 0,
            name: "",
            publishDate: new Date().toISOString().substring(0, 10),
        }
    );
    const [isEditingMode, setIsEditingMode] = useState(!id);

    useEffect(() => {
        if (loaderData) {
            setBook(loaderData);
        }
    }, [loaderData]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setBook({...book, [name]: value});
    };

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        const url = id ? `/api/books/${id}` : '/api/books';
        const method = id ? 'PUT' : 'POST';

        await fetch(url, {
            method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(book),
        });

        navigate('/books');
    };

    const handleCancel = () => {
        if (id) {
            setIsEditingMode(false);
        } else {
            navigate('/books');
        }
    };

    const handleEditBook = () => {
        setIsEditingMode(true);
    };

    const handleDeleteBook = async () => {
        if (id) {
            await fetch(`/api/books/${id}`, {
                method: 'DELETE',
            });
            navigate('/books');
        }
    };


    return (
        <div>
            <div className="form-container">
                <h1>Add new Book</h1>
                <form onSubmit={handleSubmit}>
                    <p>
                        <strong>Name:</strong> {isEditingMode ?
                        <input type="text" name="name" value={book.name} onChange={handleChange}
                               required/> : book.name}
                    </p>
                    <p>
                        <strong>Publish Date:</strong> {isEditingMode ?
                        <input type="date" name="birthDate" value={book.publishDate} onChange={handleChange}
                               required/> : book.publishDate}
                    </p>
                    {isEditingMode && (
                        <p>
                            <strong>Author ID:</strong>
                            <input type="text" name="authorId" value={book.authorId} onChange={handleChange}
                                   required/>
                        </p>
                    )}

                    {isEditingMode ? (
                        <div className="form-buttons">
                            <button className="btn btn-save" type="submit">Save</button>
                            <button className="btn btn-cancel" type="button" onClick={handleCancel}>Cancel</button>
                        </div>
                    ) : (
                        <div className="form-buttons">
                            <button className="btn btn-edit" type="button" onClick={handleEditBook}>Edit</button>
                            <button className="btn btn-delete" type="button" onClick={handleDeleteBook}>Delete
                            </button>
                        </div>
                    )}
                </form>
            </div>
        </div>
    );
};

BookDetailsPage.loader = loader;
export default BookDetailsPage;