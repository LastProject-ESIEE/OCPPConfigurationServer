import Box from "@mui/material/Box";
import { Button, Container, Grid, Paper, TextField, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { englishRoleToFrench } from "../../../sharedComponents/NavBar";

function Account() {
    const [user, setUser] = useState<any>(null);
    const [oldPassword, setOldPassword] = useState("");
    const [password1, setPassword1] = useState("");
    const [password2, setPassword2]= useState("");
    const [isButtonDisabled, setIsButtonDisabled] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    useEffect(() => {
        fetch("/api/user/me")
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw response;
            })
            .then(data => {
                setUser(data);
            })
            .catch(error => {
                console.error("ERROR ", error);
            });
    }, []);

    useEffect(() => {
        if (
            password1 === password2
            && password1 !== ""
            && validatePassword(password1)
        ) {
            setIsButtonDisabled(false);
        } else {
            setIsButtonDisabled(true);
        }
    }, [password1, password2]);

    const validatePassword = (password : string) => {
        // Regex to check prerequisites of password.
        const regex = /^(?=.*\d)(?=.*[!@#$%^&*~"'{([-|`_\\)\]}+°£µ§/:;.,?<>])(?=.*[a-z])(?=.*[A-Z]).{8,30}$/;
        return regex.test(password);
    };

    const handleButtonClick = () => {
        if (oldPassword !== "" && password1 === password2) {
            fetch("/api/user/updatePassword", {
                method: "POST",
                body: JSON.stringify({
                    oldPassword: oldPassword,
                    newPassword: password1
                }),
                headers: {
                    "Content-Type": "application/json"
                }
            }).then(response => {
                if (response.ok) {
                    // Update the state of the user.
                    setUser({ ...user, password: password1});
                    setOldPassword("");
                    setPassword1("");
                    setPassword2("");
                    setSuccessMessage("Mot de passe mis à jour avec succès.");
                    setErrorMessage("");
                } else {
                    setErrorMessage("Erreur lors de la réinitialisation du mot de passe.");
                    setSuccessMessage("");
                }
            }).catch(error => {
                console.error("ERROR ", error);
                setErrorMessage("Erreur lors de la réinitialisation du mot de passe.");
            });
            console.log("Passwords match. Update password in database.");
        } else {
            console.log("Passwords don't match.");
        }
    }

    return (
        <Container maxWidth="xl" sx={{mt: 4, mb: 4}}>
            <Grid container direction={"row"} justifyContent={"space-between"} sx={{width: "100%", textAlign: "center"}}>
                <Grid item xs={12} md={6} sx={{p: 5}}>
                    <Paper
                        elevation={0}
                        variant="outlined"
                        sx={{
                            backgroundColor: 'rgb(249, 246, 251)',
                            borderColor: 'rgba(226,229,233,255)',
                            borderWidth: 2,
                            height: "100%",
                        }}
                    >
                        <Grid container direction={"column"} textAlign={"center"} justifyContent={"space-between"} sx={{height: "100%", paddingTop: 8, paddingBottom: 8}}>
                            <Grid item>
                                <Typography variant="h6">
                                    <b>Nom :</b> {(user && user.lastName) || "Inconnu"}
                                </Typography>
                            </Grid>
                            <Grid item>
                                <Typography variant="h6">
                                    <b>Prénom :</b> {(user && user.firstName) || "Inconnu"}
                                </Typography>
                            </Grid>
                            <Grid item>
                                <Typography variant="h6">
                                    <b>Adresse mail :</b> {(user && user.email) || "Inconnu"}
                                </Typography>
                            </Grid>
                            <Grid item>
                                <Typography variant="h6">
                                    <b>Rôle :</b> {(user && englishRoleToFrench(user.role)) || "Inconnu"}
                                </Typography>
                            </Grid>
                        </Grid>
                    </Paper>
                </Grid>
                <Grid item xs={12} md={6} sx={{p: 5, alignItems: "center"}}>
                    <Grid container>
                        <Grid item>
                            <Typography variant="h6">
                                <b>Changer de mot de passe :</b>
                            </Typography>
                        </Grid>
                        <Grid item container direction={"column"}>
                            <TextField
                                id={"oldPassword"}
                                label={"Ancien mot de passe"}
                                type={"password"}
                                value={oldPassword}
                                onChange={(e) => setOldPassword(e.target.value)}
                                sx={{marginTop: 2}}
                            >
                            </TextField>
                            <TextField
                                id={"password1"}
                                label={"Nouveau mot de passe"}
                                type={"password"}
                                value={password1}
                                onChange={(e) => setPassword1(e.target.value)}
                                sx={{marginTop: 2}}
                            >
                            </TextField>
                            <TextField
                                id={"password2"}
                                label={"Nouveau mot de passe"}
                                type={"password"}
                                value={password2}
                                onChange={(e) => setPassword2(e.target.value)}
                                sx={{marginTop: 2}}
                            >
                            </TextField>

                        </Grid>
                        <Grid item>
                            <Button
                                type={"submit"}
                                disabled={isButtonDisabled}
                                onClick={handleButtonClick}
                                style={isButtonDisabled ? {
                                    backgroundColor: "#DBDBDB",
                                    color: "black",
                                    borderRadius: "30px",
                                    fontSize: "1em",
                                    marginTop: 16.5
                                } : {
                                    backgroundColor: "#C8FAC7",
                                    color: "black",
                                    borderRadius: "30px",
                                    fontSize: "0.5em",
                                    marginTop: 16.5
                                }}
                            >
                                Changer
                            </Button>
                        </Grid>
                    </Grid>
                </Grid>
                {/* TODO : Change with the toast later */}
                <Grid xs={12} md={12}>
                    <Box style={{
                        fontSize: "0.5em",
                        textAlign: "center",
                        marginTop: "20px"
                    }}>
                        {errorMessage && <div style={{ color: 'red' }}>{errorMessage}</div>}
                    </Box>
                    <Box style={{
                        fontSize: "0.5em",
                        textAlign: "center",
                        marginTop: "20px"
                    }}>
                        {successMessage && <div style={{ color: 'green' }}>{successMessage}</div>}
                    </Box>
                </Grid>
            </Grid>
        </Container>
    );
}

export default Account;