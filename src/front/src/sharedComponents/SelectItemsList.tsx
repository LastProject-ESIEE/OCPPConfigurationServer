import { Autocomplete, Box, Button, Grid, Input, Paper, TextField, Typography } from "@mui/material";
import { useState } from "react";
import DeleteIcon from '@mui/icons-material/Delete';

export type SelectItemKind = 'input' | 'values'

export type KeyValueItem<T> = {
    label: string,
    item: T,
    value: string, 
    selected: boolean,
    id: string,
    checker: (value: string) => boolean,
}

export default function SelectItemsList<T>(props: {
    title: string,
    keyTitle: string,
    items: KeyValueItem<T>[],
    selectedItems: KeyValueItem<T>[],
    selectKind: SelectItemKind,
    setSelectedItems: React.Dispatch<React.SetStateAction<KeyValueItem<T>[]>>,
    validation: (items: KeyValueItem<T>[]) => void
}){
    const [selectedItem, setSelectedItem] = useState<KeyValueItem<T> | null>(null);

    const updateItemSelection = (id: string, state: boolean) => {
        props.setSelectedItems(prevItemList => {
            return prevItemList.map(el => {
                if(el.id == id){
                    return {...el, selected: state}
                }
                return el
            })
        })
    }

    const updateItemValue = (id: string, value: string) => {
        props.setSelectedItems(prevItemList => {
            return prevItemList.map(el => {
                if(el.id == id){
                    return {...el, value: value}
                }
                return el
            })
        })
    }

    return (
        <Box>
            <Paper elevation={2} sx={{p: 2, pt: 0, mt: 3}}>
                <Grid direction={"column"} container justifyContent="space-between">
                    {/*Section displayed title*/}
                    <Grid xs={4} item>
                        <h4>{props.title} :</h4>
                    </Grid>
                    <Grid xs={7} item>
                    <Grid container alignItems="center" justifyContent="space-evenly">
                        <Grid item>
                            <Autocomplete
                                disabled={props.items.length === 0}
                                onChange={(event, value) => {
                                    setSelectedItem(value)
                                }}
                                /*disableClearable*/
                                sx={{width: 300}}
                                disablePortal
                                options={props.items.filter(item => !props.selectedItems.find(otherItem => otherItem.id == item.id))}
                                getOptionLabel={element => element ? element.label : ""}
                                value={selectedItem}
                                renderInput={(params) => <TextField {...params} label={props.keyTitle}/>}
                            />
                        </Grid>
                        <Grid item>
                            <Button size={"large"} onClick={() => {
                                    if(selectedItem){
                                        props.setSelectedItems([selectedItem, ...props.selectedItems])
                                        setSelectedItem(null)
                                    }
                                }} 
                                variant="contained" 
                                type="submit"
                                sx={{borderRadius: 100}}>
                                    <span style={{fontSize: "larger", fontWeight: "bolder"}}>+</span>
                            </Button>
                        </Grid>
                    </Grid>
                        {/*The list of selected elements*/}
                        {props.selectedItems.length !== 0 && (
                            <Grid sx={{pt: 1, pb: 1}} direction="column" container alignItems="flex-start"
                                justifyContent="space-evenly">
                                {props.selectedItems.map((item) => {
                                    return (
                                        <Box key={"selected-item-" + item.id}>
                                            <Grid sx={{pt: 1, pb: 1}} container alignItems="center">
                                                 {/*Button used to remove the element from the list*/}
                                                <Button
                                                    size={"large"}
                                                    sx={{
                                                        width: "30px", // Adjust as needed
                                                        height: "30px", // Adjust as needed
                                                        color: "error",
                                                    }}
                                                    color={"error"}
                                                    onClick={() => {
                                                        props.setSelectedItems([...(props.selectedItems.filter(i => i.id !== item.id))])
                                                    }}
                                                >
                                                    <DeleteIcon/>
                                                </Button>
                                                {/*Element label*/}
                                                <Grid item>
                                                    <Typography>{item.label}</Typography>
                                                </Grid>
                                                {/*If input mode is activated then save element input value*/}
                                                {(props.selectKind == "input") && (
                                                    <Grid item marginLeft={2}>
                                                        <Input
                                                            onChange={event => {
                                                                updateItemValue(item.id, event.target.value)
                                                            }}
                                                            value={item.value}
                                                            placeholder="valeur"/>
                                                    </Grid>
                                                )}
                                        </Grid>
                                    </Box>
                                    )
                                })}
                            </Grid>
                        )}
                    </Grid>
                </Grid>
            </Paper>
        </Box>
    )
}