import { Box, Button, Grid, IconButton } from "@mui/material";
import FormInput from "../../../sharedComponents/FormInput";
import RoleComponent from "./components/RoleComponent";
import { useEffect, useState } from "react";
import { Role, createNewUser } from "../../../conf/userController";
import { useNavigate } from "react-router-dom";
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';

function AddAccount() {
    const [lastName, setLastName] = useState("");
    const [firstName, setFirstName] = useState("");
    const [mail, setMail] = useState("");
    const [role, setRole] = useState<Role>("VISUALIZER");
    const [isButtonDisabled, setIsButtonDisabled] = useState(true);
    const [password1, setPassword1] = useState("");
    const [password2, setPassword2]= useState("");
    const [toast, setToast] = useState(false); // TODO Edit with the real toast later
    const [display, setDisplay] = useState(false); // TODO Edit with the real toast later
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
            <Grid container sx={{alignContent: "center", width: "100%"}}>
                <Grid item xs={12}>
                    <IconButton
                        onClick={() => navigate("/home/account")}
                    >
                        <ArrowBackIosNewIcon/>
                    </IconButton>
                </Grid>
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
                            let returnValue = createNewUser(user)
                            returnValue.then(value => {
                                setToast(value)
                                setDisplay(true)
                            })
                        }
                    }
                >
                    Créer
                </Button>
                </Grid>
                <Grid item xs={12} sx={{ml: "35%", mr: "35%", mt: "1%", textAlign: "center"}}>
                    <Box>
                        {toast && display && (
                            // TODO : Mettre le toast ici
                            <div style={{color: "green"}}>L'utilisateur a été créé.</div>
                            ) 
                        }
                        {!toast &&  display && (
                            // TODO : Mettre le toast ici 
                            <div style={{color: "red"}}>L'utilisateur existe déjà.</div>
                            )                         
                        }
                    </Box>
                </Grid>
            </Grid>
        </Grid>
    )
}

export default AddAccount;