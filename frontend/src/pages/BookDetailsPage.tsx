import {LoaderFunction, useLoaderData, useNavigate} from "react-router-dom";
import {useState} from "react";
import "../assets/edit-book.css"

interface Book {
    id: number;
    authorId: number;
    name: string;
    publishDate: string;
    authorName: string;
}

const loader: LoaderFunction = async function getBookDetails({params}) {
    const response = await fetch(`/api/books/${params.id}`);
    if (!response.ok) {
        throw new Error("Book not found");
    }
    const book = await response.json();
    book.publishDate = new Date(book.publishDate).toISOString().substring(0, 10);
    return book;
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
            body: JSON.stringify({
                ...editedBook,
                publishDate: new Date(editedBook.publishDate).toISOString()
            })
        });
        if (response.ok) {
            const updatedBook = await response.json();
            setEditedBook(updatedBook);
            setIsEditing(false);
        } else {
            console.error("Failed to save book");
        }
    };

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setEditedBook({...editedBook, [name]: value});
    };
    const handleCancel = () => {
        navigate('/books');
    };

    return (
        <div>
            <div className="form-container2">
                <p>
                    <strong>Publish Date:</strong> {isEditing ?
                    <input type="date" name="publishDate" value={editedBook.publishDate}
                           onChange={handleChange}/> : book.publishDate}
                </p>
                <p>
                    <strong>Name:</strong> {isEditing ?
                    <input type="text" name="name" value={editedBook.name} onChange={handleChange}/> : book.name}
                </p>

                {isEditing ? (
                    <div className="form-buttons">
                        <button className="btn btn-save" onClick={handleSaveBook}>Save</button>
                        <button className="btn btn-cancel" onClick={handleCancel}>Cancel</button>
                    </div>
                ) : (
                    <div className="form-buttons">
                        <button className="btn btn-edit" onClick={handleEditBook}>Edit</button>
                        <button className="btn btn-delete" onClick={handleDeleteBook}>Delete</button>
                    </div>
                )}
            </div>
        </div>
    );
};

BookDetailsPage.loader = loader;
export default BookDetailsPage;