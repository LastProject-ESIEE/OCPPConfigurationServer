import {Button, Grid} from "@mui/material";
import FormInput from "../../../sharedComponents/FormInput";
import RoleComponent from "./components/RoleComponent";
import {useEffect, useState} from "react";
import BackButton from "../../../sharedComponents/BackButton";
import {ApiRole, createNewUser} from "../../../conf/userController";
import {notificationManager} from "../Home";
import {useNavigate} from "react-router";

/**
 * React component that allow to create an new account for the application
 * @returns 
 */
function AddAccount() {
    const [lastName, setLastName] = useState("");
    const [firstName, setFirstName] = useState("");
    const [mail, setMail] = useState("");
    const [role, setRole] = useState<ApiRole>("VISUALIZER");
    const [isButtonDisabled, setIsButtonDisabled] = useState(true);
    const [password1, setPassword1] = useState("");
    const [password2, setPassword2]= useState("");
    const navigate = useNavigate();

    // When password values changed trigger this function
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
            {/* Display button to return to the account list */}
            <BackButton link={"/home/account"}/>
            {/* Display account edition form */}
            <Grid container sx={{alignContent: "center", width: "100%"}}>
                {/* LastName input */}
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Nom"}
                        onChange={lastName => setLastName(lastName)}
                        value={lastName}
                    />
                </Grid>
                {/* FirstName input */}
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Prénom"}
                        onChange={firstName => setFirstName(firstName)}
                        value={firstName}
                    />
                </Grid>
                {/* Email input */}
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Email"}
                        onChange={mail => setMail(mail)}
                        value={mail}
                    />
                </Grid>
                {/* Password input*/}
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Mot de passe"}
                        onChange={password => setPassword1(password)}
                        isPassword={true}
                        value={password1}
                    />
                </Grid>
                {/* Password validation input */}
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <FormInput name={"Confirmer le mot de passe"}
                        onChange={password => setPassword2(password)}
                        isPassword={true}
                        value={password2}
                    />
                </Grid>
                {/* Display user role (not editable) */}
                <Grid item xs={12} sx={{ml: "35%", mr: "35%"}}>
                    <RoleComponent role={role} setRole={setRole}/>
                </Grid>
                <Grid item xs={12} sx={{ml: "35%", mr: "35%", mt: "1%", textAlign: "center"}}>
                {/* Validation button */}
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
                            let returnValue = createNewUser(user)
                            returnValue.then(value => {
                                if (value) {
                                    notificationManager.emitNotification({
                                        type: "SUCCESS",
                                        title: firstName + " " + lastName + " ",
                                        content: "L'utilisateur a été créé."
                                    });
                                    navigate("/home/account");
                                } else {
                                    notificationManager.emitNotification({
                                        type: "ERROR",
                                        title: "Erreur ",
                                        content: "L'utilisateur n'a pas pu être créé."
                                    })
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