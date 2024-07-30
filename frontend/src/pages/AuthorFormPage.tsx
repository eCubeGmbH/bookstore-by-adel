import {FormEvent, useState} from "react";
import {LoaderFunction, useLoaderData, useNavigate, useParams} from "react-router-dom";
import "../assets/add-new-book-author.css"
import {Author} from "../components/AuthorsTable.tsx";

export const loader: LoaderFunction = async function getData({params}) {
    const response = await fetch(`/api/authors/${params.id}`);
    return response.json();
}

const AuthorFormPage = () => {
    const navigate = useNavigate();
    const {id} = useParams();
    const loaderData = useLoaderData() as Author;
    const [author, setAuthor] = useState<Author>(
        loaderData || {
            id: 0,
            name: "",
            country: "",
            birthDate: new Date().toISOString().substring(0, 10),
        }
    );

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();
        if (id) {
            await fetch(`/api/authors/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(author)
            });
        } else {
            await fetch('/api/authors', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(author),
            });
        }
        navigate('/authors');
    };


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setAuthor({...author, [name]: value});
    };

    const handleCancel = () => {
        navigate('/authors');
    };

    return (
        <div className="form-container">
            <h1>{id ? "Edit Author" : "Add New Author"}</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>
                        Name:
                        <input type="text" name="name" value={author.name} onChange={handleChange} required/>
                    </label>
                </div>
                <div>
                    <label>
                        Country:
                        <input type="text" name="country" value={author.country} onChange={handleChange} required/>
                    </label>
                </div>
                <div>
                    <label>
                        Birth Date:
                        <input type="date" name="birthDate" value={author.birthDate.toString()}
                               onChange={handleChange} required/>
                    </label>
                </div>
                <button type="submit" onClick={handleSubmit}>Save</button>
                <button type="submit" onClick={handleCancel}>Cancel</button>
            </form>
        </div>
    );
};
export default AuthorFormPage;