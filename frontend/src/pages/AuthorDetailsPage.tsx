import {LoaderFunction, useLoaderData, useNavigate} from "react-router-dom";
import {useState} from "react";


interface Author {
    id: number;
    name: string;
    country: string;
    birthDate: string;
}

const loader: LoaderFunction = async function getAuthorDetails({params}) {
    const response = await fetch(`/api/authors/${params.id}`);
    if (!response.ok) {
        throw new Error("Author not found");
    }
    const author = await response.json();
    author.birthDate = new Date(author.birthDate).toISOString().substring(0, 10);
    return author;
}

const AuthorDetailsPage = () => {
    const author = useLoaderData() as Author;
    const navigate = useNavigate();
    const [editedAuthor, setEditedAuthor] = useState<Author>(author);
    const [isEditingMode, setIsEditingMode] = useState(false);

    const handleDeleteAuthor = async () => {
        await fetch(`/api/authors/${author.id}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        navigate('/authors');
    }

    const handleEditAuthor = () => {
        setIsEditingMode(true);
    };

    const handleSaveAuthor = async () => {
        const response = await fetch(`/api/authors/${editedAuthor.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                ...editedAuthor,
                birthDate: new Date(editedAuthor.birthDate).toISOString()
            })
        });
        if (response.ok) {
            const updatedAuthor = await response.json();
            setEditedAuthor(updatedAuthor);
            setIsEditingMode(false);
        }
        navigate(`/authors/${editedAuthor.id}`);
    };

    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const {name, value} = event.target;
        setEditedAuthor({...editedAuthor, [name]: value});
    };
    const handleCancel = () => {
        navigate('/authors');
    };

    return (
        <div>
            <div className="form-container2">

                <p>
                    <strong>Name:</strong> {isEditingMode ?
                    <input type="text" name="name" value={editedAuthor.name}
                           onChange={handleChange}/> : author.name}
                </p>
                <hr/>
                <p>
                    <strong>Publish Date:</strong> {isEditingMode ?
                    <input type="date" name="publishDate" value={editedAuthor.birthDate}
                           onChange={handleChange}/> : author.birthDate}
                </p>
                <hr/>
                <p>
                    <strong>Country:</strong> {isEditingMode ?
                    <input type="text" name="country" value={editedAuthor.country}
                           onChange={handleChange}/> : author.country}
                </p>
                <hr/>

                {isEditingMode ? (
                    <div className="form-buttons">
                        <button className="btn btn-save" onClick={handleSaveAuthor}>Save</button>
                        <button className="btn btn-cancel" onClick={handleCancel}>Cancel</button>
                    </div>
                ) : (
                    <div className="form-buttons">
                        <button className="btn btn-edit" onClick={handleEditAuthor}>Edit</button>
                        <button className="btn btn-delete" onClick={handleDeleteAuthor}>Delete</button>
                    </div>
                )}
            </div>
        </div>
    );
};

AuthorDetailsPage.loader = loader;
export default AuthorDetailsPage;