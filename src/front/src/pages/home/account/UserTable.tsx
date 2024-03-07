import { Box, Grid, MenuItem, Select, SelectChangeEvent, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import {
    DEFAULT_FILTER_SELECT_VALUE,
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import { apiRoleToFrench, ApiRole, searchUser, User, frenchToEnglishRole, FrenchRole } from "../../../conf/userController";
import DeleteUserModalComponent from "./components/DeleteUserModalComponent";
import { searchElements } from "../../../conf/backendController";


const PAGE_SIZE = 30; // Max items displayed in the user table

const userTableColumns: TableColumnDefinition[] = [
    {
        title: "Nom",
        filter: {
            apiField: "lastName",
            filterType: "input"
        }
    },
    {
        title: "Prénom",
        filter: {
            apiField: "firstName",
            filterType: "input"
        }
    },
    {
        title: "Rôle",
        filter: {
            apiField: "role",
            filterType: "select",
            restrictedValues: [
                DEFAULT_FILTER_SELECT_VALUE,
                apiRoleToFrench("ADMINISTRATOR"),
                apiRoleToFrench("EDITOR"),
                apiRoleToFrench("VISUALIZER"),
            ],
            apiValueFormatter: value => {
                return value === DEFAULT_FILTER_SELECT_VALUE ? "" : frenchToEnglishRole(value as FrenchRole)
            }
        }
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
    const [userRoleList, setUserRoleList] = useState<ApiRole[]>([]);
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


    function onChangeEvent(event: SelectChangeEvent<ApiRole>, user: User) {
        let role = event.target.value as ApiRole
        fetch(`/api/user/${user.id}/role/${role}`, {
            method: "PATCH",
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
                <Box key={"box-configuration-edit-path-" + user.id} margin={1} maxWidth={"true"}>
                    <Box style={{maxWidth: "true", margin: 3, borderRadius: 50, color: 'black', backgroundColor: '#E1E1E1'}}>
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
                                                    {apiRoleToFrench(role)}
                                                </MenuItem>
                                            )
                                        }
                                    )}
                                </Select>
                            </Grid>
                            <Grid item xs={12/userTableColumns.length} maxWidth={"true"} justifyContent={"center"} textAlign={"center"}>
                                <DeleteUserModalComponent
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
        fetchData: filters => {
            const nextPage = currentPage + 1;
            searchElements<User>("/api/user/search", {page: nextPage, size: PAGE_SIZE, filters: filters}).then((result: PageRequest<User> | undefined) => {
                if(!result){
                    setError("Erreur lors de la récupération des utilisateurs.")
                    return
                }
                setTableData([...tableData, ...result.data])
                setHasMore(result.total > PAGE_SIZE * (nextPage + 1))
            });
            setCurrentPage(nextPage)
        },
        onFiltering: filters => {
            // Reset page and search
            setCurrentPage(0)
            console.log(filters)
            searchElements<User>("/api/user/search", {page: 0, size: PAGE_SIZE, filters: filters}).then((result: PageRequest<User> | undefined) => {
                if(!result){
                    setError("Erreur lors de la récupération des utilisateurs.")
                    return
                }
                setTableData(result.data)
                setHasMore(result.total > PAGE_SIZE)
            });
        }
    }

    return InfinityScrollItemsTable(props)
}

export default UserTable;
