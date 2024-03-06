import { Box, Button, Grid, IconButton, MenuItem, Modal, Select, SelectChangeEvent, Typography } from "@mui/material";
import { Dispatch, SetStateAction, useEffect, useState } from "react";
import {
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import { Role, searchUser, User } from "../../../conf/userController";
import { englishRoleToFrench } from "../../../sharedComponents/NavBar";
import DeleteIcon from '@mui/icons-material/Delete';
import CheckIcon from '@mui/icons-material/Check';
import CloseIcon from '@mui/icons-material/Close';

const PAGE_SIZE = 30; // Max items displayed in the user table

const userTableColumns: TableColumnDefinition[] = [
    {
        title: "Nom",
    },
    {
        title: "Prénom",
    },
    {
        title: "Rôle",
    },
    {
        title: "", // extra column for delete button later
    }
]

function UserTable() {
    const [tableData, setTableData] = useState<User[]>([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [error, setError] = useState<string | undefined>(undefined);
    const [userRoleList, setUserRoleList] = useState<Role[]>([]);
    const [me, setMe] = useState<User | undefined>(undefined);


    useEffect(() => {
        searchUser(PAGE_SIZE).then((result: PageRequest<User> | undefined) => {
            if(!result){
                setError("Erreur lors de la récupération des utilisateurs.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
        });
    }, [])

    useEffect(() => {
        const fetchRoleList = async () => {
            const response = await fetch('/api/user/allRoles');
            const data = await response.json();
            setUserRoleList(data);
        }
        fetchRoleList();
    }, []);

    useEffect(() => {
        const fetchCurrentUser = async () => {
            const response = await fetch('/api/user/me');
            const data = await response.json();
            setMe(data);
        }
        fetchCurrentUser();
    }, []);

    function onChangeEvent(event: SelectChangeEvent<Role>, user: User) {
        let role = event.target.value as Role
        fetch("/api/user/updateRole", {
            method: "POST",
            body: JSON.stringify({
                id: user.id,
                role: role
            }),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => {
            if (response.ok) {
                // TODO : refactor to use the DTO in the response

                if (!user) {
                    return;
                }
                let updatedUser = tableData.find(u => u.id === user.id)
                if (!updatedUser) {
                    console.log("User id for the update notification is not found.")
                    return
                }
                // Update role of user
                user.role = role
                setTableData([...tableData])
            }
        })
    }

    let props: InfinityScrollItemsTableProps<User> = {
        columns: userTableColumns,
        key: "user-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        onSelection: user => { console.log("Selected item : " + user.id) },
        formatter: (user) => {
            return (
                <Box key={"box-configuration-edit-path-" + user.id}  paddingTop={1} maxWidth={"true"}>
                    <Box style={{maxWidth: "true", height:"5vh", padding: 0, paddingTop: 3, borderRadius: 50, color: 'black', backgroundColor: '#E1E1E1'}}>
                        <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                            <Grid item xs={12/userTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                                <Typography variant="body1" align="center">{user.lastName}</Typography>
                            </Grid>
                            <Grid item xs={12/userTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                                <Typography variant="body1" align="center">{user.firstName}</Typography>
                            </Grid>
                            <Grid item xs={12/userTableColumns.length} maxWidth={"true"} justifyContent={"center"}>
                                <Select
                                    disabled={user.id === me?.id}
                                    value={user.role}
                                    style={{
                                        border: 0,
                                        textAlign: "center",
                                        marginTop: 10,
                                        marginBottom: 10
                                    }}
                                    onChange={event => {onChangeEvent(event, user)}}
                                    fullWidth={true} size="small" variant="standard">

                                    {userRoleList && userRoleList
                                        .map(role => {
                                            return (
                                                <MenuItem
                                                    key={"menuItem" + role.toString()}
                                                    value={role}
                                                    disabled={role === user.role}
                                                    style={{
                                                        border: 0
                                                    }}
                                                >
                                                    {englishRoleToFrench(role.toString())}
                                                </MenuItem>
                                            )
                                        }
                                    )}
                                </Select>
                            </Grid>
                            <Grid item xs={12/userTableColumns.length} maxWidth={"true"} justifyContent={"center"} textAlign={"center"}>
                                <DeleteUserModal
                                    user={user} 
                                    enabled={user.id === me?.id} 
                                    setTableData={setTableData}
                                    setError={setError}
                                    setHasMore={setHasMore}
                                />
                            </Grid>
                        </Grid>
                    </Box>
                </Box>
            )
        },
        fetchData: () => {
            const nextPage = currentPage + 1;
            searchUser(PAGE_SIZE,nextPage).then((result: PageRequest<User> | undefined) => {
                if(!result){
                    setError("Erreur lors de la récupération des utilisateurs.")
                    return
                }
                setTableData([...tableData, ...result.data])
                setHasMore(result.total > PAGE_SIZE * (nextPage + 1))
            });
            setCurrentPage(nextPage)
        },
    }

    return InfinityScrollItemsTable(props)
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

function DeleteUserModal(props: {
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
                        <Typography>{"Rôle : " + englishRoleToFrench(props.user.role.toString())}</Typography>
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
export default UserTable;