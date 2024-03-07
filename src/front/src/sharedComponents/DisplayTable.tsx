import { Box, Grid, MenuItem,Skeleton, Select, TextField, Typography } from "@mui/material";
import InfiniteScroll from "react-infinite-scroll-component";
import { SearchFilter } from "../conf/backendController";
import { useState } from "react";

export const DEFAULT_FILTER_SELECT_VALUE = "Aucun"

export type TableColumnFilterDefinition = {
    apiField: string,
    filterType: "input" | "select",
    restrictedValues?: string[], // must be set for select input
    apiValueFormatter?: (value: string) => string, // must be set for select input to transform input value to api value
}

export type TableColumnDefinition = {
    title: string,
    size?: number,
    filter?: TableColumnFilterDefinition,
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
    error: string | undefined,
    onSelection: (item: T) => void, // function call when an element is selected in the list
    formatter: (item: T, index: number) => JSX.Element // function that transform an item to a JSX element
    fetchData: (filters: SearchFilter[]) => void, // function that fetch next items
    onFiltering?: (filters: SearchFilter[]) => void, // function call when filters changed
}


export function InfinityScrollItemsTable<T>(props: InfinityScrollItemsTableProps<T>) {
    const [filters, setFilters] = useState<SearchFilter[]>([]);

    const updateFilter = (filterField: string, filterValue: string) => {
        console.log(filterField,filterValue)
        if(props.onFiltering){
            // If filter not present add it
            if(!filters.find(v => v.filterField === filterField)){
                setFilters([...filters, {filterField: filterField ?? "", filterValue: filterValue}])
                props.onFiltering([...filters, {filterField: filterField ?? "", filterValue: filterValue}])
                return
            }
            // Otherwise update filter
            setFilters(prevFilters => {
                const newValues = prevFilters.map(filter => {
                    if(filter.filterField === filterField){
                        return {...filter, filterValue: filterValue}
                    }
                    return filter
                })

                // As function is async props.onFiltering should be call here
                if(props.onFiltering){
                    props.onFiltering(newValues)
                }
                return newValues
            })
        }
    }

    return (
        <Box maxWidth={"true"} paddingTop={2} marginLeft={2}>
            <Box marginRight={2}>
                {/*Display table columns*/}
                <Grid key={"table-header-columns"} container flexDirection={"row"} maxWidth={"true"} >
                    {props.columns.map(column => {
                        return (
                            <Grid xs={column?.size ?? 12/props.columns.length} item key={"table-header-column-" + column.title} justifyContent={"center"}>
                                <Typography variant="h6" textAlign={"center"}>{column.title}</Typography>
                                <Box>
                                    {column.filter && (
                                        <TableColumnFilter
                                            column={column}
                                            onFilterValidate={filterValue => {
                                                if(column.filter){
                                                    updateFilter(column.filter.apiField, filterValue)
                                                }
                                            } }
                                        />
                                    )}
                                </Box>
                            </Grid>
                        )
                    })}
                </Grid>
            </Box>
             {/*Display table content*/}
            <Box key={"box-items-scrollable-list"} maxWidth={"true"} marginRight={2} marginLeft={2}>
                <div id={"scrollableDiv"}
                key={props.key + "-scrollableDiv-list"}
                style={{
                    height: "75vh",
                    overflow: 'auto',
                }}>
                    <InfiniteScroll
                            key="scrollable-items-list"
                            style={{overflow:"hidden", border: 1, borderColor: "black", maxWidth: "true"}}
                            dataLength={props.data.length}
                            next={() => props.fetchData(filters)}
                            hasMore={props.hasMore}
                            loader={
                                <>
                                    {(props.error !== undefined) && (
                                        <Typography variant="h6" color={"red"} textAlign={"center"}>{props.error}</Typography>
                                    )}
                                    {(props.error === undefined) && (
                                        <Box key={"box-skeleton-list"} maxWidth={"true"} marginRight={2} marginLeft={2}>
                                            <Box display="flex" flexDirection="column">
                                                {Array.from(Array(30).keys()).map((_, index) => (
                                                    <Box key={"skeleton-list-" + index} marginY={1}>
                                                        <Skeleton sx={{borderRadius: 50}} variant="rectangular" width={"100%"} height={"5vh"} />
                                                    </Box>
                                                ))}
                                            </Box>
                                        </Box>
                                    )}
                                </>
                            }
                            scrollableTarget={"scrollableDiv"}
                        >
                        {props.data.map((item: T, index) => props.formatter(item, index))}
                    </InfiniteScroll>
                    {(props.data.length === 0) && !props.hasMore && (
                        <Typography variant="h6" color={"black"} textAlign={"center"}>Aucun élément...</Typography>
                    )}
                </div>
            </Box>
        </Box>
    );
}


function TableColumnFilter(props: {column: TableColumnDefinition, onFilterValidate: (value: string) => void}){
    const [filterValue, setFilterValue] = useState(DEFAULT_FILTER_SELECT_VALUE);
    return (
        <Grid container maxWidth={"true"} justifyContent={"center"}>
            {(props.column.filter?.filterType === "input") && (
                <TextField
                placeholder={props.column.title}
                size="small"
                onKeyDown={event => {
                    if(event.key === 'Enter'){
                        props.onFilterValidate(filterValue)
                    }
                }}
                onBlur={() => {
                    props.onFilterValidate(filterValue)
                }}
                onChange={event => {
                    setFilterValue(event.target.value)
                }}
            />
            )}
            {(props.column.filter?.filterType === "select" && (props.column.filter?.restrictedValues?.length ?? 0) > 0) && (
                <Select
                    size="small"
                    variant="outlined"
                    fullWidth
                    value={filterValue}
                    onChange={event => {
                        let value = event.target.value
                        setFilterValue(value)
                        if(props.column.filter?.apiValueFormatter){
                            props.onFilterValidate(props.column.filter.apiValueFormatter(value))
                            return
                        }
                        props.onFilterValidate(value)
                    }}>

                    {props.column.filter.restrictedValues && props.column.filter.restrictedValues.map((item) => {
                        let selected = filterValue === item;
                        return (
                            <MenuItem key={"columnfilterOption-"+ item} value={item} selected={selected}>{item}</MenuItem>
                        )}
                    )}
            </Select>
            )}

        </Grid>
    )
}