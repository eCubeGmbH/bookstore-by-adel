import {LoaderFunction, useLoaderData, useNavigate} from "react-router-dom";
import { useState } from "react";

interface Book {
    id: number;
    authorId: number;
    name: string;
    publishDate: string;
    authorName: string;
}

const loader: LoaderFunction = async function getBookDetails({ params }) {
    const response = await fetch(`/api/books/${params.id}`);
    if (!response.ok) {
        throw new Error("Book not found");
    }
    return response.json();
}

const BookDetailsPage = () => {
    const book = useLoaderData() as Book;
    const navigate = useNavigate();
    const [editedBook, setEditedBook] = useState<Book>(book);
    const [isEditing, setIsEditing] = useState(false);

    const handleDeleteBook = async () => {
        await fetch(`/api/books/${book.id}`, {
            method: 'DELETE'
        });
        navigate('/books');
    };

    const handleEditBook = () => {
        setIsEditing(true);
    };

    const handleSaveBook = async () => {
        const response = await fetch(`/api/books/${editedBook.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(editedBook),
        });
        if (response.ok) {
            const updatedBook = await response.json();
            setEditedBook(updatedBook);
            setIsEditing(false);
        } else {
            // Handle error case
            console.error("Failed to save book");
        }
    };

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setEditedBook({ ...editedBook, [name]: value });
    };

    return (
        <div>
            <h1>{isEditing ? <input type="text" name="name" value={editedBook.name} onChange={handleChange} /> : book.name}</h1>
            <p>
                <strong>Publish Date:</strong> {isEditing ? <input type="date" name="publishDate" value={editedBook.publishDate} onChange={handleChange} /> : book.publishDate}
            </p>
            <p>
                <strong>Author:</strong> {isEditing ? <input type="text" name="authorName" value={editedBook.authorName} onChange={handleChange} /> : book.authorName}
            </p>

            {isEditing ? (
                <button onClick={handleSaveBook}>Save</button>
            ) : (
                <>
                    <button onClick={handleEditBook}>Edit</button>
                    <button onClick={handleDeleteBook}>Delete</button>
                </>
            )}
        </div>
    );
};

BookDetailsPage.loader = loader;
export default BookDetailsPage;
