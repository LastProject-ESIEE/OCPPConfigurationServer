import { Box, Grid, Tooltip, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import {
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import { TechnicalLog } from "../../../conf/technicalLogController";
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import ArrowDropUpIcon from '@mui/icons-material/ArrowDropUp';
import { searchElements } from "../../../conf/backendController";

const PAGE_SIZE = 30; // Max items displayed in the technical log table

const technicalLogTableColumns: TableColumnDefinition[] = [
    {
        title: "Date",
        size: 2,
        filter: {
            apiField: "date",
            filterType: "date"
        },
        sort: {
            apiField: "date",
        }
    },
    {
        title: "Composant",
        size: 3,
        filter: {
            apiField: "component",
            filterType: "input"
        },
        sort: {
            apiField: "component",
        }
    },
    {
        title: "Criticité",
        size: 2,
        filter: {
            apiField: "level",
            filterType: "input"
        },
        sort: {
            apiField: "level",
        }
    },
    {
        title: "Log",
        size: 4.5,
        filter: {
            apiField: "completeLog",
            filterType: "input"
        },
        sort: {
            apiField: "completeLog",
        }
    },
    {
        title: "",
        size: 0.5,
    }
]

function TechnicalLogTable() {
    const [tableData, setTableData] = React.useState<TechnicalLog[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);

    useEffect(() => {
        searchElements<TechnicalLog>("/api/log/technical/search",
            {
                page: 0,
                size: PAGE_SIZE,
                sort: { field: 'date', order: "desc" }
            }).then((result: PageRequest<TechnicalLog> | undefined) => {
            if(!result){
                setError("Erreur lors de la récupération des logs techniques.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
        });
    }, [])


    let props: InfinityScrollItemsTableProps<TechnicalLog> = {
        columns: technicalLogTableColumns,
        key: "technical-log-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        formatter: (technicalLog, index) => {
            return (
                <Box key={"box-technical-log-" + index} paddingTop={1} maxWidth={"true"}>
                    <LogLineItemVMamar technicalLog={technicalLog}/>
                </Box>
            )
        },
        fetchData: (filters, sort) => {
            const nextPage = currentPage + 1;
            searchElements<TechnicalLog>("/api/log/technical/search", {page: nextPage, size: PAGE_SIZE, filters: filters, sort: sort}).then((result: PageRequest<TechnicalLog> | undefined) => {
                if(!result){
                    setError("Erreur lors de la récupération des logs techniques.")
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
            searchElements<TechnicalLog>("/api/log/technical/search", {page: 0, size: PAGE_SIZE, filters: filters, sort: sort}).then((result: PageRequest<TechnicalLog> | undefined) => {
                if(!result){
                    setError("Erreur lors de la récupération des logs techniques.")
                    return
                }
                setTableData(result.data)
                setHasMore(result.total > PAGE_SIZE)
            });
        }
    }

    return InfinityScrollItemsTable(props)
}

function LogLineItemVMamar(props: { technicalLog: TechnicalLog }) {
    const [open, setOpen] = useState(false);

    return (
        <Box onClick={() => setOpen(!open)}
             style={{
                 paddingTop: 3,
                 paddingBottom: 3,
                 borderRadius: open ? 25 : 50,
                 backgroundColor: '#E1E1E1'
             }}>
                <Grid container maxWidth={"true"}
                      flexDirection={"row"}
                      alignItems={"center"}>
                    <Grid item xs={technicalLogTableColumns[0].size}
                          justifyContent={"center"}>
                        <Typography variant="body1"
                                    align="center"
                                    noWrap={true}>
                            {new Date(props.technicalLog.date).toLocaleString()}</Typography>
                    </Grid>
                    <Grid item xs={technicalLogTableColumns[1].size}
                          justifyContent={"center"}>
                        <Typography variant="body1"
                                    align="center"
                                    noWrap={true}>
                            {props.technicalLog.component}</Typography>
                    </Grid>
                    <Grid item xs={technicalLogTableColumns[2].size}
                          justifyContent={"center"}>
                        <Typography variant="body1"
                                    align="center"
                                    noWrap={true}>
                            {props.technicalLog.level}</Typography>
                    </Grid>
                    <Grid item xs={technicalLogTableColumns[3].size}
                          justifyContent={"center"}>
                        {!open
                            ? <Tooltip title={props.technicalLog.completeLog}>
                                <Typography variant="body1"
                                            align="center"
                                            noWrap={true}>
                                    {props.technicalLog.completeLog}
                                </Typography>
                            </Tooltip>
                            : <Typography variant="inherit"
                                          align="center"
                                          noWrap={true}
                                          color={'rgb(130,130,130)'}>
                                {props.technicalLog.completeLog}
                            </Typography>}
                    </Grid>
                    <Grid item xs={technicalLogTableColumns[4].size}>
                            <Typography align={"center"}>
                                {!open
                                    ? <ArrowDropDownIcon/>
                                    : <ArrowDropUpIcon/>}
                            </Typography>
                    </Grid>
                </Grid>
            {open && (
                <Typography align={"center"}
                            marginLeft={"10vh"}
                            marginRight={"10vh"}
                            paddingTop={1}
                            paddingBottom={1}>
                    {props.technicalLog.completeLog}</Typography>
            )}
        </Box>
    )
}


export default TechnicalLogTable;