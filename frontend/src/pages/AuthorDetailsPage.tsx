import {LoaderFunction, useLoaderData, useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import "../assets/add-new-book-author.css"

interface Author {
    id: number;
    name: string;
    country: string;
    birthDate: string;
}

// Loader function to fetch author data for editing
export const loader: LoaderFunction = async ({params}) => {
    if (params.id) {
        const response = await fetch(`/api/authors/${params.id}`);
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Failed to load author data");
        }
    }
    return null;
};

const AuthorDetailsPage = () => {
    const navigate = useNavigate();
    const {id} = useParams<{ id: string }>();
    const loaderData = useLoaderData() as Author | null;

    const [author, setAuthor] = useState<Author>(
        loaderData || {
            id: 0,
            name: "",
            country: "",
            birthDate: new Date().toISOString().substring(0, 10),
        }
    );

    const [isEditingMode, setIsEditingMode] = useState(!id);

    useEffect(() => {
        if (loaderData) {
            setAuthor(loaderData);
        }
    }, [loaderData]);

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setAuthor({...author, [name]: value});
    };


    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        const url = id ? `/api/authors/${id}` : '/api/authors';
        const method = id ? 'PUT' : 'POST';

        await fetch(url, {
            method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(author),
        });

        navigate('/authors');
    };

    const handleCancel = () => {
        if (id) {
            setIsEditingMode(false);
        } else {
            navigate('/authors');
        }
    };

    const handleEditAuthor = () => {
        setIsEditingMode(true);
    };

    const handleDeleteAuthor = async () => {
        if (id) {
            await fetch(`/api/authors/${id}`, {
                method: 'DELETE',
            });
            navigate('/authors');
        }
    };


    return (
        <div>
            <div className="form-container">
                <h1>Add new Author</h1>
                <form onSubmit={handleSubmit}>
                    <p>
                        <strong>Name:</strong> {isEditingMode ?
                        <input type="text" name="name" value={author.name} onChange={handleChange}
                               required/> : author.name}
                    </p>
                    <p>
                        <strong>Birth date:</strong> {isEditingMode ?
                        <input type="date" name="birthDate" value={author.birthDate} onChange={handleChange}
                               required/> : author.birthDate}
                    </p>
                    <p>
                        <strong>Country:</strong> {isEditingMode ?
                        <input type="text" name="country" value={author.country} onChange={handleChange}
                               required/> : author.country}
                    </p>
                    {isEditingMode ? (
                        <div className="form-buttons">
                            <button className="btn btn-save" type="submit">Save</button>
                            <button className="btn btn-cancel" type="button" onClick={handleCancel}>Cancel</button>
                        </div>
                    ) : (
                        <div className="form-buttons">
                            <button className="btn btn-edit" type="button" onClick={handleEditAuthor}>Edit</button>
                            <button className="btn btn-delete" type="button" onClick={handleDeleteAuthor}>Delete
                            </button>
                        </div>
                    )}
                </form>
            </div>
        </div>
    );
};
AuthorDetailsPage.loader = loader;
export default AuthorDetailsPage;