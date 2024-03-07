import { Dispatch, SetStateAction, useState } from "react";
import { User, searchUser } from "../../../../conf/userController";
import { Box, Button, Grid, IconButton, Modal, Typography } from "@mui/material";
import DeleteIcon from '@mui/icons-material/Delete';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';
import { englishRoleToFrench } from "../../../../sharedComponents/NavBar";
import { PageRequest } from "../../../../sharedComponents/DisplayTable";

const PAGE_SIZE = 30; // Max items displayed in the user table

function DeleteUserModalComponent(props: {
    user: User,
    enabled: boolean,
    setTableData: Dispatch<SetStateAction<User[]>>,
    setError: Dispatch<SetStateAction<string | undefined>>,
    setHasMore: Dispatch<SetStateAction<boolean>>
}){
    const [open, setOpen] = useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    return (
        <Box>
            <IconButton
                disabled={props.enabled}
                onClick={handleOpen}
            >
                <DeleteIcon color={(!props.enabled) ? "error" : "disabled"}/>
            </IconButton>
            <Modal
                open={open}
                onClose={handleClose}
            >
                <Grid sx={{
                        boxShadow: 10,
                        bgcolor: "background.paper",
                        border: "2px solid #FFF",
                        p: 4,
                        maxWidth: 550,
                        justifyContent: "center",
                        textAlign: "center",
                        position: 'absolute' as 'absolute',
                        top: '50%',
                        left: '50%',
                        transform: 'translate(-50%, -50%)'
                    }}
                    container
                    >
                    <Box>
                        <Typography variant="h6" sx={{paddingBottom: 2}}>Êtes-vous sûr de vouloir supprimer cet utilisateur ?</Typography>
                        <Typography>{"Nom : " + props.user.lastName}</Typography>
                        <Typography>{"Prénom : " + props.user.firstName}</Typography>
                        <Typography>{"Rôle : " + englishRoleToFrench(props.user.role)}</Typography>
                        <Grid container>
                            <Grid item xs={12} md={6}>
                                <Button
                                    sx={{
                                        borderRadius: 28,
                                        marginTop: 2
                                    }}
                                    variant="contained"
                                    color="success"
                                    onClick={async () => {
                                        let value = await deleteUser(props.user)
                                        if (value) {
                                            searchUser(PAGE_SIZE).then((result: PageRequest<User> | undefined) => {
                                                if(!result){
                                                    props.setError("Erreur lors de la récupération des utilisateurs.")
                                                    return
                                                }
                                                props.setTableData(result.data)
                                                props.setHasMore(result.total > PAGE_SIZE)
                                            });
                                        }
                                    }}
                                >
                                    <CheckIcon/>
                                </Button>
                            </Grid>
                            <Grid item xs={12} md={6}>
                                <Button
                                    sx={{
                                        borderRadius: 28,
                                        marginTop: 2
                                    }}
                                    variant="contained"
                                    color="error"
                                    onClick={handleClose}
                                >
                                    <CloseIcon/>
                                </Button>
                            </Grid>
                        </Grid>
                    </Box>
                </Grid>
            </Modal>
        </Box>
    )
}



async function deleteUser(user: User) {
    let request = await fetch("/api/user/" + user.id,
        {
            method: "DELETE"
        })
    if (request.ok) {
        return true
    } else {
        console.error("Couldn't delete user, error code: " + request.status)
        return false
    }
}

export default DeleteUserModalComponent;