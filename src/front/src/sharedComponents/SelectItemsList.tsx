import { Autocomplete, Box, Button, Grid, Input, Paper, TextField, Typography } from "@mui/material";
import { useState } from "react";
import DeleteIcon from '@mui/icons-material/Delete';

export type SelectItemKind = 'input' | 'values'

export type KeyValueItem<T> = {
    label: string, //Item label displayed in the select box
    item: T, // Item cotained in the list
    value: string, // Input value for the corresponding item
    id: string, // Item id (must be unique)
    checker: (value: string) => boolean, // Checker of the input value (only works for the list kind input)
    // a message on checker error can be added here
}

export default function SelectItemsList<T>(props: {
    title: string,
    keyTitle: string,
    items: KeyValueItem<T>[],
    selectedItems: KeyValueItem<T>[],
    selectKind: SelectItemKind,
    setSelectedItems: React.Dispatch<React.SetStateAction<KeyValueItem<T>[]>>,
}){
    const [selectedItem, setSelectedItem] = useState<KeyValueItem<T> | null>(null);
    const updateItemValue = (id: string, value: string) => {
        props.setSelectedItems(prevItemList => {
            return prevItemList.map(el => {
                if (el.id === id) {
                    return {...el, value: value}
                }
                return el
            })
        })
    }

    return (
        <Box>
            <Paper elevation={2} sx={{p: 2, pt: 0, mt: 3, background: "rgb(249, 246, 251)"}}>
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
                                options={props.items.filter(item => !props.selectedItems.find(otherItem => otherItem.id === item.id))}
                                getOptionLabel={element => element ? element.label : ""}
                                value={selectedItem}
                                renderInput={(params) => <TextField {...params} label={props.keyTitle}/>}
                            />
                        </Grid>
                        <Grid item>
                            <Button size={"large"} onClick={() => {
                                    if (selectedItem) {
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
                                {props.selectedItems.map((item, index) => {
                                    return (
                                        <ListItem 
                                            key={"select-list-element-"+index}
                                            selectKind={props.selectKind}
                                            item={item}
                                            onInputChange={newValue => {
                                                updateItemValue(item.id, newValue)
                                            }}
                                            onRemove={() => {
                                                props.setSelectedItems([...(props.selectedItems.filter(i => i.id !== item.id))])
                                            }}
                                        />
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


function ListItem<T>(props: {item: KeyValueItem<T>, selectKind: SelectItemKind, onInputChange: (value: string) => void, onRemove: () => void}){
    const [backgroundColor, setBackgroundColor] = useState("rgb(249, 246, 251)");
    console.log(props.item)
    return (
        <Box>
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
                        props.onRemove()
                    }}
                >
                    <DeleteIcon/>
                </Button>
                {/*Element label*/}
                <Grid item>
                    <Typography>{props.item.label}</Typography>
                </Grid>
                {/*If input mode is activated then save element input value*/}
                {(props.selectKind === "input") && (
                    <Grid item marginLeft={2}>
                        <Input
                            style={{backgroundColor: backgroundColor, borderRadius: 5}}
                            onChange={event => {
                                props.onInputChange(event.target.value)
                                if (!props.item.checker(event.target.value)) {
                                    setBackgroundColor("rgba(255, 0, 0, 0.2)")
                                    return
                                }
                                setBackgroundColor("rgb(249, 246, 251)")
                            }}
                            value={props.item.value}
                            placeholder="valeur"
                            />
                    </Grid>
                )}
            </Grid>
    </Box>
    )
}