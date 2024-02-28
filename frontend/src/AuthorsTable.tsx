export default function AuthorsTable({authors}: {
    authors: Array<any>
}) {
    return (
        <div>
            <h2>Authors Table</h2>
            <table>
                <thead>
                <tr>
                    <th>Author ID</th>
                    <th>Name</th>
                    <th>Country</th>
                    <th>Birth Date</th>
                </tr>
                </thead>
                <tbody>
                {authors.map((author) => (
                    <tr key={author.id}>
                        <td>{author.id}</td>
                        <td>{author.name}</td>
                        <td>{author.country}</td>
                        <td>{`${author.birthDate[0]}-${author.birthDate[1]}-${author.birthDate[2]}`}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};
