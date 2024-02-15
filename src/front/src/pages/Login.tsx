import React, { useEffect, useState } from 'react';

function Login() {
    const [failed, setFailed] = useState(false);

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const failed = urlParams.has('failed');
        setFailed(failed);
    }, []);

    return (
        <div style={{fontFamily: 'Arial, sans-serif', backgroundColor: '#f0f0f0'}}>
            <div style={{
                maxWidth: '400px',
                margin: '50px auto',
                padding: '20px',
                backgroundColor: '#fff',
                borderRadius: '5px',
                boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)'
            }}>
                <h2 style={{textAlign: 'center', marginBottom: '20px'}}>Login</h2>
                <form action="/authentication/login/process" method="post">
                    <label htmlFor="username">Username</label>
                    <input type="text" id="username" name="username" placeholder="Enter your username" style={{
                        width: '100%',
                        padding: '10px',
                        margin: '8px 0',
                        boxSizing: 'border-box',
                        border: '1px solid #ccc',
                        borderRadius: '4px'
                    }} required/>

                    <label htmlFor="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="Enter your password" style={{
                        width: '100%',
                        padding: '10px',
                        margin: '8px 0',
                        boxSizing: 'border-box',
                        border: '1px solid #ccc',
                        borderRadius: '4px'
                    }} required/>

                    <input type="submit" value="Login" style={{
                        width: '100%',
                        backgroundColor: '#4CAF50',
                        color: 'white',
                        padding: '14px 20px',
                        margin: '8px 0',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer'
                    }}/>
                </form>

                {failed && (
                    <div id="error-message" style={{
                        marginTop: '10px',
                        padding: '10px',
                        border: '1px solid red',
                        borderRadius: '4px',
                        color: 'red',
                        fontWeight: 'bold',
                        display: 'block'
                    }}>
                        Invalid username or password. Please try again.
                    </div>
                )}
            </div>
        </div>
    );
}

export default Login;
