import { Button, Grid } from "@mui/material";
import FormInput from "../../../sharedComponents/FormInput";
import RoleComponent from "./components/RoleComponent";
import { useEffect, useState } from "react";
import BackButton from "../../../sharedComponents/BackButton";
import { ApiRole, User } from "../../../conf/userController";
import { wsManager } from "../Home";
import { useNavigate } from "react-router";
import { createNewElement } from "../../../conf/backendController";

function AddAccount() {
    const [lastName, setLastName] = useState("");
    const [firstName, setFirstName] = useState("");
    const [mail, setMail] = useState("");
    const [role, setRole] = useState<ApiRole>("VISUALIZER");
    const [isButtonDisabled, setIsButtonDisabled] = useState(true);
    const [password1, setPassword1] = useState("");
    const [password2, setPassword2]= useState("");
    const navigate = useNavigate();

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

    return (
        <Grid>
            <BackButton link={"/home/account"}/>
            <Grid container sx={{alignContent: "center", width: "100%"}}>
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Nom"}
                        onChange={lastName => setLastName(lastName)}
                        value={lastName}
                    />
                </Grid>
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Prénom"}
                        onChange={firstName => setFirstName(firstName)}
                        value={firstName}
                    />
                </Grid>
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Email"}
                        onChange={mail => setMail(mail)}
                        value={mail}
                    />
                </Grid>
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Mot de passe"}
                        onChange={password => setPassword1(password)}
                        isPassword={true}
                        value={password1}
                    />
                </Grid>
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Confirmer le mot de passe"}
                        onChange={password => setPassword2(password)}
                        isPassword={true}
                        value={password2}
                    />
                </Grid>
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <RoleComponent role={role} setRole={setRole}/>
                </Grid>
                <Grid item xs={12} sx={{ml: "35%", mr: "35%", mt: "1%", textAlign: "center"}}>
                <Button 
                    sx={{borderRadius: 28}} 
                    variant="contained" 
                    color="primary"
                    disabled={isButtonDisabled}
                    onClick={() => {
                            const user = {
                                firstName: firstName,
                                lastName: lastName, 
                                email: mail, 
                                password: password1, 
                                role: role
                            }
                            // let returnValue = createNewUser(user)
                            // returnValue.then(value => {
                            //     if (value) {
                            //         wsManager.emitNotification({
                            //             type: "SUCCESS",
                            //             title: firstName + " " + lastName + " ",
                            //             content: "L'utilisateur a été créé."
                            //         });
                            //         navigate("/home/account");
                            //     } else {
                            //         wsManager.emitNotification({
                            //             type: "ERROR",
                            //             title: "Erreur ",
                            //             content: "L'utilisateur n'a pas pu être créé."
                            //         })
                            //     }
                            // })
                        createNewElement<User>("/api/user/create", user)
                            .then(userRequest => {
                                if (userRequest.succes) {
                                    let user = userRequest.succes
                                    wsManager.emitNotification({
                                        type: "SUCCESS",
                                        title: user.email + " ",
                                        content: "L'utilisateur a été créé."
                                    });
                                    navigate("/home/account");
                                }
                                if (userRequest.error) {
                                    wsManager.emitNotification({
                                        type: "ERROR",
                                        title: "Erreur ",
                                        content: userRequest.error.message
                                    });
                                }
                            })
                        }
                    }
                >
                    Créer
                </Button>
                </Grid>
            </Grid>
        </Grid>
    )
}

export default AddAccount;