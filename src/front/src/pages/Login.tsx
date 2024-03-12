import React, { useEffect, useState } from 'react';
import Box from "@mui/material/Box";
import {Grid} from "@mui/material";

function Login() {
    const [failed, setFailed] = useState(false);

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const failed = urlParams.has('failed');
        setFailed(failed);
    }, []);

    return (
        <Box>
            <Grid style={{
                alignContent: 'center',
                marginLeft: '20%',
                marginRight: '20%'
            }}>
                <Grid xs={12}>
                    <Box textAlign={"center"}>
                        <img src="/assets/logo-brs.png" alt="logo de BRS" style={{maxWidth: "40%"}}/>
                    </Box>
                </Grid>
                <Grid xs={12}>
                    <Box>
                        <form action="/authentication/login/process" method="post" style={{
                            marginLeft: '30%',
                            marginRight: '30%'
                        }}>
                            <label htmlFor="username">Identifiant :</label>
                            <input type="text" id="username" name="username" placeholder="Entrez votre identifiant mail@BRS" style={{
                                width: '100%',
                                padding: '10px',
                                margin: '8px 0',
                                boxSizing: 'border-box',
                                border: '1px solid #ccc',
                                borderRadius: '4px'
                            }} required/>

                            <label htmlFor="password">Mot de passe :</label>
                            <input type="password" id="password" name="password" placeholder="Entrez votre mot de passe"
                                   style={{
                                       borderRadius: '4px',
                                       width: '100%',
                                       padding: '10px',
                                       margin: '8px 0',
                                       boxSizing: 'border-box',
                                       border: '1px solid #ccc'
                                   }} required/>

                            <input type="submit" value="Connexion" style={{
                                width: '100%',
                                backgroundColor: '#36A9E1',
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
                                display: 'block',
                                textAlign: 'center'
                            }}>
                                Mauvais identifiant et/ou mot passe. Veuillez réessayer
                            </div>
                        )}
                    </Box>
                </Grid>
            </Grid>
        </Box>
    );
}

export default Login;
