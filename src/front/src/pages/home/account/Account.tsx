import {Button, Container, Grid, Paper, TextField, Typography} from "@mui/material";
import React, {useEffect, useState} from "react";
import {englishRoleToFrench} from "../../../sharedComponents/NavBar";
import {wsManager} from "../Home";
import { getUserInformation } from "../../../conf/backendController";
import { User } from "../../../conf/userController";

function Account() {
    const [user, setUser] = useState<User | undefined>(undefined);
    const [oldPassword, setOldPassword] = useState("");
    const [password1, setPassword1] = useState("");
    const [password2, setPassword2]= useState("");
    const [isButtonDisabled, setIsButtonDisabled] = useState(true);

    useEffect(() => {
        getUserInformation().then(userInfo => {
            if (!userInfo) {
                console.error("Failed to retrieve user information.")
                return
            }
            setUser({
                email: userInfo.email,
                firstName: userInfo.firstName,
                lastName: userInfo.lastName,
                id: userInfo.id,
                role: userInfo.role,
                password: "",
            })
        })
    }, []);

    useEffect(() => {
        if (
            password1 === password2
            && password1 !== ""
        ) {
            setIsButtonDisabled(false);
        } else {
            setIsButtonDisabled(true);
        }
    }, [password1, password2]);

    const handleButtonClick = () => {
        if(!user){
            console.error("User not defined.")
            return
        }
        if (oldPassword !== "" && password1 === password2) {
            fetch("/api/user/updatePassword/" + user.id, {
                method: "PATCH",
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
                    wsManager.emitNotification({
                        type: "SUCCESS",
                        title: "Succès ",
                        content: "Le mot de passe a été mis à jour."
                    });
                } else {
                    wsManager.emitNotification({
                        type: "ERROR",
                        title: "Erreur ",
                        content: "Le mot de passe n'a pas pu être modifié. Nouveau mot de passe non conforme ou mauvais ancien mot de passe de l'utilisateur."
                    })
                }
            }).catch(error => {
                console.error("Error : " + error);
            });
        }
    }

    return (
        <Container 
            maxWidth="xl" 
            sx={{mt: 4, mb: 4}}
        >
            <Grid 
                container 
                direction={"row"} 
                justifyContent={"space-between"} 
                textAlign={"center"}
            >
                <Grid 
                    item 
                    xs={12} 
                    md={6} 
                    sx={{
                        p: 5, 
                        pl: 20, 
                        pr: 10
                    }}
                >
                    <Paper
                        elevation={0}
                        variant="outlined"
                        sx={{
                            backgroundColor: 'rgb(249, 246, 251)',
                            borderColor: 'rgba(226,229,233,255)',
                            borderWidth: 2,
                            height: "100%"
                        }}
                    >
                        <Grid 
                            container 
                            direction={"column"} 
                            textAlign={"center"} 
                            justifyContent={"space-between"}
                            sx={{
                                height: "100%", 
                                paddingTop: 8, 
                                paddingBottom: 8
                            }}
                        >
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
                <Grid 
                    item 
                    xs={12} 
                    md={6} 
                    sx={{
                        p: 5, 
                        pl: 20, 
                        pr: 10
                    }}
                >
                    <Grid container justifyContent={"center"}>
                        <Grid item xs={12}>
                            <Typography variant="h6">
                                <b>Changer de mot de passe :</b>
                            </Typography>
                        </Grid>
                        <Grid item container direction={"column"} xs={12}>
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
                        <Grid item xs={12}>
                            <Paper
                                elevation={0}
                                variant="outlined"
                                sx={{
                                    backgroundColor: 'rgb(249, 246, 251)',
                                    borderColor: 'rgba(226,229,233,255)',
                                    borderWidth: 2,
                                    marginTop: 2,
                                    marginBottom: 1
                                }}
                            >
                                <Grid>
                                    <Typography variant="body1">
                                        Le mot de passe doit contenir, au minimum, 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial. Au maximum, vous pouvez inscrire 30 caractères.
                                    </Typography>
                                </Grid>
                            </Paper>
                        </Grid>
                        <Grid item xs={12}>
                            <Button
                                type={"submit"}
                                disabled={isButtonDisabled}
                                onClick={handleButtonClick}
                                style={isButtonDisabled ? {
                                    backgroundColor: "#DBDBDB",
                                    color: "black",
                                    borderRadius: "30px",
                                    fontSize: "1em",
                                    marginTop: 2
                                } : {
                                    backgroundColor: "#C8FAC7",
                                    color: "black",
                                    borderRadius: "30px",
                                    fontSize: "1em",
                                    marginTop: 2
                                }}
                            >
                                Changer
                            </Button>
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>
        </Container>
    );
}

export default Account;