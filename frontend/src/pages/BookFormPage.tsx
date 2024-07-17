import {FormEvent, useState} from "react";
import {LoaderFunction, useLoaderData, useNavigate, useParams} from "react-router-dom";
import {Book} from "../components/BooksTable";
import "../assets/add-new-book.css"

export const loader: LoaderFunction = async function getData({params}) {
    const response = await fetch(`/api/books/${params.id}`);
    return response.json();
}

const BookFormPage = () => {
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

    const handleSubmit = async (event: FormEvent) => {
        event.preventDefault();
        if (id) {
            await fetch(`/api/books/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(book)
            });
        } else {
            await fetch('/api/books', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(book),
            });
        }
        navigate('/books');
    };


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setBook({...book, [name]: value});
    };

    const handleCancel = () => {
        navigate('/books');
    };

    return (
        <div className="form-container">
            <h1>{id ? "Edit Book" : "Add New Book"}</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>
                        Name:
                        <input type="text" name="name" value={book.name} onChange={handleChange} required/>
                    </label>
                </div>
                <div>
                    <label>
                        Publish Date:
                        <input type="date" name="publishDate" value={book.publishDate.toString()}
                               onChange={handleChange} required/>
                    </label>
                </div>
                <button type="submit">Save</button>
                <button type="submit" onClick={handleCancel} >Cancel</button>
            </form>
        </div>
    );
};
export default BookFormPage;