import { Autocomplete, Box, Button, Container, Grid, Input, Paper, Select, TextField, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import FormInput from "../../../sharedComponents/FormInput";
import { TypeAllowed, getTypeAllowed, postCreateFirmware } from "../../../conf/FirmwareController";
import DeleteIcon from '@mui/icons-material/Delete';

type CreateOrEditFirmwareProps = {
    version: string,
    url: string,
    constructeur: string,
}


export type CreateFirmwareFormData = {
    version: string,
    url: string,
    constructeur: string,
}


export default function CreateFirmware(props: {data?: CreateFirmwareFormData}) {
    const [formData, setFormData] = useState<CreateFirmwareFormData>(props.data ?? {
        version: "",
        url: "",
        constructeur: "",
    });

    const [typeAllowedList, setTypeAllowedList] = useState<KeyValueItem<TypeAllowed>[]>([]);
    

    //const [configuration, setConfiguration] = useState<Configuration>(noConfig);

    // Fetch first charge point page on component load
    useEffect(() => {
        getTypeAllowed().then((result: TypeAllowed[] | undefined) => {
            if(!result){
                return
            }
            setTypeAllowedList(result.map(v => {
                return {
                    item: v,
                    selected: false,
                    label: v.constructor + "-" + v.type,
                    value: "",
                    id: v.id + "",
                    checker: inputValue => inputValue !== ""
                }
            }))
        });
        }, [])

    return (
        <Container maxWidth="xl" sx={{mt: 4, mb: 4}}>
            <Grid container spacing={15}>
                <Grid item xs={12} md={6}>
                    <Box>
                        <FormInput name={"Version"}
                                   onChange={val => setFormData(prevState => {
                                       prevState.version = val
                                       return prevState
                                   })}
                                   checkIsWrong={value => value === "abc"}
                                   value={formData.version}
                        />
                        <FormInput name={"URL"}
                                   onChange={val => setFormData(prevState => {
                                       prevState.url = val
                                       return prevState
                                   })}
                                   checkIsWrong={value => value === "abc"}
                        />
                        <FormInput name={"Constructeur"}
                                   onChange={val => setFormData(prevState => {
                                       prevState.constructeur = val
                                       return prevState
                                   })}
                                   checkIsWrong={value => value === "abc"}
                        />
                    </Box>
                    <Box
                        sx={{
                            display: 'flex',
                            justifyContent: 'center',
                            boxSizing: 'border-box',
                        }}
                        pt={2}
                    >
                        <Button sx={{borderRadius: 28}} variant="contained" color="primary"
                                onClick={() => postCreateFirmware(formData)}>Valider</Button>
                    </Box>

                </Grid>
                <Grid item xs={12} md={6}>
                    <SelectItemsList 
                        title={"Modèles compatibles"}
                        keyTitle={"Modèles"}
                        items={typeAllowedList} 
                        backgroundColor={"red"} 
                        selectKind="input"
                        setItems={setTypeAllowedList}
                        itemAdded={el => console.log(el)}
                        itemRemoved={el => console.log(el)}
                        itemLabelFormatter={el => el.constructor + "-" + el.type}
                        itemIdFormatter={el => el.id.toString()}
                    />
                </Grid>
            </Grid>
        </Container>
    );
}

type KeyValueItem<T> = {
    label: string,
    item: T,
    value: string, 
    selected: boolean,
    id: string,
    checker: (value: string) => boolean,
}

type SelectItemKind = 'input' | 'values'

function SelectItemsList<T>(props: {
    title: string,
    keyTitle: string,
    items: KeyValueItem<T>[],
    setItems: React.Dispatch<React.SetStateAction<KeyValueItem<T>[]>>,
    selectKind: SelectItemKind,
    backgroundColor: string,
    itemAdded: (element: T) => void,
    itemRemoved: (element: T) => void,
    itemLabelFormatter: (element: T) => string,
    itemIdFormatter: (element: T) => string,
}){
    const [selectedItem, setSelectedItem] = useState<KeyValueItem<T> | undefined>(undefined);
    const [selectedItems, setSelectedItems] = useState<KeyValueItem<T>[]>([]);
    const [inputValue, setInputValue] = useState("");

    const updateItemSelection = (id: string, state: boolean) => {
        props.setItems(prevItemList => {
            return prevItemList.map(el => {
                if(el.id == id){
                    return {...el, selected: state}
                }
                return el
            })
        })
    }

    useEffect(() => {
        selectedItems.forEach(item => {
            updateItemSelection(item.id, item.selected)
        })
    }, [selectedItems])

    return (
        <Box>
            {/*Box d'ajout d'élément*/}
            <Paper elevation={2} sx={{p: 2, pt: 0, mt: 3}}>
                <Grid direction={"column"} container justifyContent="space-between">
                    <Grid xs={4} item>
                        <h4>{props.title} :</h4>
                    </Grid>
                    
                      {/*Liste d'éléments*/}
                    <Grid xs={7} item>
                    <Grid container alignItems="center" justifyContent="space-evenly">
                        <Grid item>
                                <Autocomplete
                                    disabled={props.items.length === 0}
                                    onChange={(event, value) => {
                                        if(!value){
                                            return
                                        }
                                        setInputValue(value.label)
                                        setSelectedItem(value)
                                    }}
                                    disableClearable
                                    sx={{width: 300}}
                                    disablePortal
                                    inputValue={inputValue}
                                    options={props.items}
                                    getOptionLabel={(element: KeyValueItem<T>) => element.label}
                                    value={undefined}
                                    isOptionEqualToValue={(element1, element2) => element1.id === element2.id}
                                    renderInput={(params) => <TextField {...params} label={props.keyTitle}/>}
                                />
                            </Grid>
                            <Grid item>
                                <Button size={"large"} onClick={() => {
                                    console.log("ADD ITEM", selectedItem)
                                    if(selectedItem){
                                        setInputValue("")
                                        //updateItemSelection(selectedItem.id, true)
                                        selectedItem.selected = true
                                        setSelectedItems([...selectedItems, selectedItem])
                                        setSelectedItem(undefined)
                                    }
                                }} variant="contained" type="submit"
                                        sx={{borderRadius: 100}}>
                                    <span style={{fontSize: "larger", fontWeight: "bolder"}}>+</span>
                                </Button>
                            </Grid>
                        </Grid>
                        {selectedItems.length !== 0 && (
                            <Grid sx={{pt: 1, pb: 1}} direction="column" container alignItems="flex-start"
                                justifyContent="space-evenly">
                                {selectedItems.map((item) => {
                                    return (
                                        <Box key={"selected-item-" + item.id}>
                                            <Grid sx={{pt: 1, pb: 1}} container alignItems="center">
                                                <Button
                                                    size={"large"}
                                                    sx={{
                                                        width: "30px", // Adjust as needed
                                                        height: "30px", // Adjust as needed
                                                        color: "error",
                                                    }}
                                                    color={"error"}
                                                    onClick={() => {
                                                        item.selected = false
                                                        setSelectedItems([...(selectedItems.filter(i => i.id !== item.id))])
                                                        //updateItemSelection(item.id, false)
                                                        console.log(props.items)
                                                    // setSelectedKeys(selectedKeys.filter(key => key.id !== selectedKey.id))
                                                    }}
                                                >
                                                    <DeleteIcon/>
                                                    
                                                </Button>
                                                <Grid item>
                                                    <Typography>{item.label}</Typography>
                                                </Grid>
                                                {(props.selectKind == "input") && (
                                                    <Grid item>
                                                    <Input
                                                        onChange={v => console.log("CHANGED")}
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


/*

function RightSection(props: { globalState: GlobalState; setGlobalState: Dispatch<SetStateAction<GlobalState>> , selectedKeys: Transcriptor[], setSelectedKeys: Dispatch<SetStateAction<Transcriptor[]>> }) {
    const backgroundColor = 'rgb(249, 246, 251)'
    
    return (
        <Box>
            <Paper elevation={2} sx={{p: 2, pt: 0, mt: 3, backgroundColor}}>
                <Grid direction={"column"} container justifyContent="space-between">
                    <Grid xs={4} item>
                        <h4>Champs de la configuration :</h4>
                    </Grid>
                    <Grid xs={7} item>

                        <Box sx={{pt: 1, pb: 1}} style={{maxHeight: '60vh', overflow: 'auto'}}>
                            <AddKeyValuePair setSelectedKeys={props.setSelectedKeys} selectedKeys={props.selectedKeys}/>
                            {props.selectedKeys.length !== 0 && (
                                <Grid sx={{pt: 1, pb: 1}} direction="column" container alignItems="center"
                                      justifyContent="space-evenly">
                                    {props.selectedKeys.map((key) => {
                                        return (
                                            <KeyValuePairComponent
                                                key={key.id}
                                                value={""}
                                                globalState={props.globalState}
                                                selectedKeys={props.selectedKeys}
                                                setSelectedKeys={props.setSelectedKeys}
                                                selectedKey={key}
                                                setGlobalState={props.setGlobalState}/>
                                        )
                                    })}
                                </Grid>
                            )}
                        </Box>
                    </Grid>
                </Grid>
            </Paper>
        </Box>
    );
}
*/