import {
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import React, { useEffect } from "react";
import { Box, Grid, ListItemButton, Tooltip, Typography } from "@mui/material";
import { Link } from "react-router-dom";
import { Configuration } from "../../../conf/configurationController";
import { searchElements } from "../../../conf/backendController";


const PAGE_SIZE = 30; // Max items displayed in the configuration table

const configurationTableColumns: TableColumnDefinition[] = [
    {
        title: "Nom",
        size: 3,
        filter: {
            apiField: "name",
            filterType: "input"
          },
        sort: {
            apiField: "name",
        }
    },
    {
        title: "Description",
        size: 4,
        filter: {
            apiField: "description",
            filterType: "input"
        },
        sort: {
            apiField: "description",
        }
    },
    {
        title: "Configuration",
        size: 3,
        filter: {
            apiField: "",
            filterType: "input",
            disable: true,
        },
    },
    {
        title: "Dernière modification",
        size: 2,
        filter: {
            apiField: "lastEdit",
            filterType: "input"
        },
        sort: {
            apiField: "lastEdit",
        }
    },
]

function ConfigurationTable() {
    const [tableData, setTableData] = React.useState<Configuration[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);

    useEffect(() => {
        searchElements<Configuration>("/api/configuration/search",
            {
                page: 0,
                size: PAGE_SIZE,
                sort: { field: "lastEdit", order: "desc" }
            }).then((result: PageRequest<Configuration> | undefined) => {
            if(!result){
                setError("Erreur lors de la récupération des configurations.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
        });
    }, [])


    let props: InfinityScrollItemsTableProps<Configuration> = {
        columns: configurationTableColumns,
        key: "configuration-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        formatter: (configuration) => {
            return (
                <Box key={"box-configuration-edit-path-" + configuration.id} paddingTop={1} maxWidth={"true"}>
                    <Link key={"configuration-edit-path-" + configuration.id} to={{pathname: 'edit/' + configuration.id}}
                          style={{textDecoration: 'none', paddingTop: 10}}>
                        <ListItemButton style={{
                            maxWidth: "true",
                            height: "5vh",
                            padding: 0,
                            paddingTop: 3,
                            borderRadius: 50,
                            color: 'black',
                            backgroundColor: '#E1E1E1'
                        }}>
                            <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                                <Grid item xs={configurationTableColumns[0].size} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Typography variant="body1" align="center" noWrap={true}>{configuration.name}</Typography>
                                </Grid>
                                <Grid item xs={configurationTableColumns[1].size} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Tooltip title={configuration.description}>
                                        <Typography variant="body1" align="center" noWrap={true}>{configuration.description}</Typography>
                                    </Tooltip>
                                </Grid>
                                <Grid item xs={configurationTableColumns[2].size} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Tooltip title={configuration.configuration}>
                                        <Typography variant="body1" align="center" noWrap={true}>{configuration.configuration}</Typography>
                                    </Tooltip>
                                </Grid>
                                <Grid item xs={configurationTableColumns[3].size} maxWidth={"true"}
                                      justifyContent={"center"}>
                                    <Typography variant="body1" align="center" noWrap={true}>{new Date(configuration.lastEdit).toLocaleString()}</Typography>
                                </Grid>
                            </Grid>
                        </ListItemButton>
                    </Link>
                </Box>
            )
        },
        fetchData: (filters, sort) => {
            const nextPage = currentPage + 1;
            searchElements<Configuration>("/api/configuration/search", {page: nextPage, size: PAGE_SIZE, filters: filters, sort: sort}).then((result: PageRequest<Configuration> | undefined) => {
                if (!result) {
                    setError("Erreur lors de la récupération des configurations.")
                    return
                }
                setTableData([...tableData, ...result.data])
                setHasMore(result.total > PAGE_SIZE * (nextPage + 1))
            });
            setCurrentPage(nextPage)
        },
        onFiltering: (filters, sort) => {
            // Reset page and search
            setCurrentPage(0)
            searchElements<Configuration>("/api/configuration/search", {page: 0, size: PAGE_SIZE, filters: filters, sort: sort}).then((result: PageRequest<Configuration> | undefined) => {
                if(!result){
                    setError("Erreur lors de la récupération des configurations.")
                    return
                }
                setTableData(result.data)
                setHasMore(result.total > PAGE_SIZE)
            });
        },
    }

    return InfinityScrollItemsTable(props)
}

export default ConfigurationTable;