import { Box, Grid, TextField, Typography } from "@mui/material";
import React from "react";
import InfiniteScroll from "react-infinite-scroll-component";

export type ChargePointModel = {
    id: string,
    name: string,
}

export type TableColumnFilterDefinition = {
    apiField: string,
    onChange: (newValue: string) => void,
}

export type TableColumnDefinition = {
    title: string,
    filter?: TableColumnFilterDefinition,
}

export type ConfigurationModel = {
    id: string,
    name: string,
}

export type PageRequest<T> = {
    total: number,
    page: number,
    size: number,
    data: T[],
    next: string
}

export type InfinityScrollItemsTableProps<T> = {
    key: string, // table key
    columns: TableColumnDefinition[], // table columns definition
    data: T[], // items displayed in the table
    hasMore: boolean, // boolean that check if there is no more element
    onSelection: (item: T) => void, // function call when an element is selected in the list
    formatter: (item: T, index: number) => JSX.Element // function that transform an item to a JSX element
    fetchData: () => T[], // function that fetch next items
}


export function InfinityScrollItemsTable<T>(props: InfinityScrollItemsTableProps<T>) {
    return (
        <Box maxWidth={"true"}>
            {/*Display table columns*/}
            <Grid key={"table-header-columns"} container flexDirection={"row"} maxWidth={"true"} paddingBottom={1} >
                {props.columns.map(column => {
                    return (
                        <Grid xs={12/props.columns.length} item key={"table-header-column-" + column.title} justifyContent={"center"}>
                            <Typography variant="h6" textAlign={"center"}>{column.title}</Typography>
                            {column.filter && (
                                <Grid container maxWidth={"true"} justifyContent={"center"}>
                                    <TextField placeholder={column.title} size="small">
                                    </TextField>
                                </Grid>
                            )}
                        </Grid>
                    )
                })}
            </Grid>
             {/*Display table content*/}
            <Box key={"box-items-scrollable-list"} maxWidth={"true"} border={1} borderColor="gray" borderRadius={2} marginRight={2} marginLeft={2} padding={1}>
                <div id={"scrollableDiv"}
                key={props.key + "-scrollableDiv-list"}                
                style={{
                    height: "75vh",
                    overflow: 'auto',
                }}>
                    <InfiniteScroll
                            key="scrollable-items-list"
                            style={{border: 1, borderColor: "black", maxWidth: "true"}}
                            dataLength={props.data.length}
                            next={() => props.fetchData()}
                            hasMore={props.hasMore}
                            loader={<h5>Chargement...</h5>}
                            scrollableTarget={"scrollableDiv"}
                        >
                        {props.data.map((item: T, index) => props.formatter(item, index))}
                    </InfiniteScroll>
                </div>
            </Box>
        </Box>
    );
}