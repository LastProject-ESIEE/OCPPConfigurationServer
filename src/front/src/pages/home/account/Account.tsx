import Box from "@mui/material/Box";
import { Button, Grid, TextField } from "@mui/material";
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
                setErrorMessage("Erreur lors de la réinitialisation du mot de passe.");
            });
        }
    }

    return (
        <Box style={{
            fontSize: "3em"
        }}>
            <Grid container spacing={2} marginTop={"10%"}>
                <Grid item xs={6} md={6}>
                    <Box marginLeft={"60%"}>
                        <b>Nom :</b>
                    </Box>
                </Grid>
                <Grid item xs={6} md={2}>
                    <Box>
                        {(user && user.lastName) || "Inconnu"}
                    </Box>
                </Grid>
                <Grid item xs={6} md={6}>
                    <Box marginLeft={"60%"}>
                        <b>Prénom :</b>
                    </Box>
                </Grid>
                <Grid item xs={6} md={2}>
                    <Box>
                        {(user && user.firstName) || "Inconnu"}
                    </Box>
                </Grid>
                <Grid item xs={6} md={6}>
                    <Box marginLeft={"60%"}>
                        <b>Adresse mail :</b>
                    </Box>
                </Grid>
                <Grid item xs={6} md={2}>
                    <Box>
                        {(user && user.email) || "Inconnu"}
                    </Box>
                </Grid>
                <Grid item xs={6} md={6}>
                    <Box marginLeft={"60%"}>
                        <b>Rôle :</b>
                    </Box>
                </Grid>
                <Grid item xs={6} md={2}>
                    <Box>
                        {(user && englishRoleToFrench(user.role)) || "Inconnu"}
                    </Box>
                </Grid>
                <Grid item xs={6} md={6}>
                    <Box marginLeft={"60%"}>
                        <b>Mot de passe :</b>
                    </Box>
                </Grid>
                <Grid item xs={6} md={2} alignItems={"stretch"}>
                    <TextField
                        id={"oldPassword"}
                        label={"Ancien mot de passe"}
                        type={"password"}
                        value={oldPassword}
                        onChange={(e) => setOldPassword(e.target.value)}
                    >
                    </TextField>
                </Grid>
                <Grid item xs={5} md={1}>
                    <Button
                        type={"submit"}
                        disabled={isButtonDisabled}
                        onClick={handleButtonClick}
                        style={isButtonDisabled ? {
                                backgroundColor: "#DBDBDB",
                                color: "black",
                                borderRadius: "30px",
                                fontSize: "0.5em"
                            } : {
                                backgroundColor: "#C8FAC7",
                                color: "black",
                                borderRadius: "30px",
                                fontSize: "0.5em"
                        }}
                    >
                        Changer
                    </Button>
                </Grid>
                <Grid item xs={3} md={6}>
                    <Box marginLeft={"60%"}>
                    </Box>
                </Grid>
                <Grid item xs={6} md={2} alignItems={"stretch"}>
                    <TextField
                        id={"password1"}
                        label={"Nouveau mot de passe"}
                        type={"password"}
                        value={password1}
                        onChange={(e) => setPassword1(e.target.value)}
                    >
                    </TextField>
                </Grid>
                <Grid item xs={3} md={6}>
                    <Box marginLeft={"60%"}>
                    </Box>
                </Grid>
                <Grid item xs={6} md={2}>
                    <TextField
                        id={"password2"}
                        label={"Nouveau mot de passe"}
                        type={"password"}
                        value={password2}
                        onChange={(e) => setPassword2(e.target.value)}
                    >
                    </TextField>
                </Grid>
                <Grid xs={12}>
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
        </Box>
    );
}

export default Account;