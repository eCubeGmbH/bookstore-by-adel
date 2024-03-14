import React, {useRef, useState} from 'react';
import {Author} from "./AuthorsTable.tsx";
import './index.css'

const AddAuthor = () => {
    const [isVisible, setIsVisible] = useState(false);
    const nameRef = useRef<HTMLInputElement>(null);
    const countryRef = useRef<HTMLSelectElement>(null);
    const birthdateRef = useRef<HTMLInputElement>(null);
    const countries = [
        'USA',
        'Canada',
        'UK',
        'Germany',
        'France',
        'Australia',
    ];

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const newAuthor: Author = {
            id: '',
            name: nameRef.current!.value,
            country: countryRef.current!.value,
            birthDate: new Date(birthdateRef.current!.value)
        }
        try {
            const response = await fetch('/api/authors', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newAuthor)
            });
            if (!response.ok) {
                throw new Error('Failed to create author');
            }
            console.log('Author created');
            setIsVisible(false);
        } catch (error) {
            throw new Error('Failed to create author');
        }
    }

    const handleCreateClick = () => {
        setIsVisible(true);
    };

    const handleCancelClick = () => {
        setIsVisible(false);
    };

    return (
        <>
            <button className="create-author-button" onClick={handleCreateClick}>Create Author</button>
            {isVisible && (
                <div className="overlay">
                    <section className="modal">
                        <h2>Add New Author</h2>
                        <form onSubmit={handleSubmit}>
                            <p>
                                <label>Name: </label>
                                <input type="text" ref={nameRef} required/>
                            </p>
                            <p>
                                <label>Birthdate: </label>
                                <input type="date" ref={birthdateRef} required/>
                            </p>
                            <p>
                                <label>Country: </label>
                                <select ref={countryRef} required>
                                    <option value="">Select Country</option>
                                    {countries.map(country => (
                                        <option key={country} value={country}>{country}</option>))}
                                </select>
                            </p>
                            <p>
                                <button type="submit">Submit</button>
                                <button type="button" onClick={handleCancelClick}>Cancel</button>
                            </p>
                        </form>
                    </section>
                </div>
            )}
        </>
    );
};

export default AddAuthor;