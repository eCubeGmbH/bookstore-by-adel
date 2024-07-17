import React, {useRef} from 'react';
import {Author} from "./AuthorsTable.tsx";
import '../assets/authorsDisplay.css';

interface ModifyAuthorProps {
    author: Author | null,
    onClickCancel: () => void,
    buttonLabel: string,
    buttonHandler: (author: Author) => void,
    title: string,
}

export default function ModifyAuthor({author, onClickCancel, buttonLabel, buttonHandler, title}: ModifyAuthorProps) {
    // Refs to input fields
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
    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        const authorData: Author = {
            id: author?.id || 0,
            name: nameRef.current!.value,
            country: countryRef.current!.value,
            birthDate: new Date(birthdateRef.current!.value)
        }
        buttonHandler(authorData);
    }

    const defaultBirthdate = author
        ? new Date(author.birthDate).toISOString().split('T')[0]
        : '';
    const defaultAuthorCountry = author
        ? author.country
        : '';
    //JSX
    return (
        <div className="overlay">
            <section className="modal">
                <h2>{title}</h2>
                <form onSubmit={handleSubmit}>
                    <p>
                        <label>Name: </label>
                        <input type="text" ref={nameRef} required defaultValue={author?.name || ''}/>
                    </p>
                    <p>
                        <label>Birthdate: </label>
                        <input type="date" ref={birthdateRef} required defaultValue={defaultBirthdate}/>
                    </p>
                    <p>
                        <label>Country: </label>
                        <select ref={countryRef} required defaultValue={defaultAuthorCountry}>

                            <option value=''>Select Country</option>
                            {countries.map(country => (
                                <option key={country} value={country}>{country}</option>))}
                        </select>
                    </p>
                    <p>
                        <button type='button' onClick={handleSubmit}>{buttonLabel}</button>
                        <button type="button" onClick={onClickCancel}>Cancel</button>
                    </p>
                </form>
            </section>
        </div>
    );
}
