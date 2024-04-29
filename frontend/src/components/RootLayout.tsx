import Navbar from "./NavBar.tsx";
import {Outlet} from "react-router-dom";

export default function RootLayout() {
    return (
        <>
            <Navbar/>
            <main>
                <Outlet/>
            </main>
        </>
    )
}