import Navbar from "./NavBar.tsx";
import {Outlet} from "react-router-dom";
import {ToastContainer} from "react-toastify";

export default function RootLayout() {
    return (
        <>
            <ToastContainer/>
            <Navbar/>
            <main>
                <Outlet/>
            </main>
        </>
    )
}