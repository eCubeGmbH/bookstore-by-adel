import React, {useEffect, useState} from 'react';

const MyTableComponent = () => {
    const [tableData, setTableData] = useState([]);

    useEffect(() => {
        fetch('/api/books/8200698372601688327')
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                console.log(data); // Log the received data
                setTableData(data);
            })
            .catch((error) => console.error('Error fetching data:', error));
    }, []); // Empty dependency array ensures the effect runs only once on mount

    if (!Array.isArray(tableData)) {
        // Handle the case where the fetched data is not an array
        return <div>Error: Data format is not as expected.</div>;
    }

    return (
        <div>
            <h2>Data Table</h2>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Author ID</th>
                    <th>Name</th>
                    <th>Publish Date</th>
                </tr>
                </thead>
                <tbody>
                {tableData.map((row) => (
                    <tr key={row.id}>
                        <td>{row.id.toString()}</td>
                        <td>{row.authorId}</td>
                        <td>{row.name}</td>
                        <td>{`${row.publishDate[0]}-${row.publishDate[1]}-${row.publishDate[2]}`}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default MyTableComponent;