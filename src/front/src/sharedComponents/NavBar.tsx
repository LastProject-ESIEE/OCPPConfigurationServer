import { Link, useLocation } from "react-router-dom";
import React, { useEffect, useState } from "react";
import { ButtonData, buttons } from "../conf/homeDefinition";
import {
    Box,
    Button,
    Collapse,
    Container,
    Grid,
    IconButton,
    List,
    ListItem,
    ListItemButton,
    Menu,
    Toolbar
} from "@mui/material";
import AppBar from "@mui/material/AppBar";
import Typography from "@mui/material/Typography";
import { ExpandLess, ExpandMore } from "@mui/icons-material";
import MenuIcon from '@mui/icons-material/Menu';
import LogoutIcon from '@mui/icons-material/Logout';
import { apiRoleToFrench } from "../conf/userController";

export function NavBar() {
    const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(null);
    const location = useLocation();
    const [currentButton, setCurrentButton] = useState<ButtonData | undefined>(undefined);
    const [userRole, setUserRole] = useState<any>(null);
    const [user, setUser] = useState<any>(null);

    // Update the currentButton state when the URL changes
    useEffect(() => {
        fetch("/api/user/me")
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw response;
            })
            .then(data => {
                setUser(data);
                setUserRole(data.role);
            })
            .catch(error => {
                console.error("ERROR ", error);
                setUser({firstName: "Prénom", lastName: "Nom", role: "ADMINISTRATOR"});
                setUserRole("ADMINISTRATOR");
            });
        const matchingButton = buttons.find(item => {
            const buttonPath = `/home${item.href}`;
            return location.pathname.startsWith(buttonPath);
        });
        setCurrentButton(matchingButton);
    }, [location.pathname]);

    const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
      setAnchorElNav(event.currentTarget);
    };

    const handleCloseNavMenu = () => {
      setAnchorElNav(null);
    };

    return (
      <AppBar position="static">
          <Container maxWidth={false} style={{paddingLeft: 10, paddingRight: 10}}>
              <Toolbar disableGutters>
                  {/*Mini menu*/}
                  <Box key={"little-menu-bar"} sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
                      <IconButton
                      key={"litle-menu-bar-icon"}
                      size="large"
                      aria-label="account of current user"
                      aria-controls="menu-appbar"
                      aria-haspopup="true"
                      onClick={handleOpenNavMenu}
                      color="inherit"
                      >
                      <MenuIcon />
                        </IconButton>
                      <Menu
                      id="menu-appbar"
                      key={"menu-appbar"}
                      anchorEl={anchorElNav}
                      anchorOrigin={{
                          vertical: 'bottom',
                          horizontal: 'left',
                      }}
                      keepMounted
                      transformOrigin={{
                          vertical: 'top',
                          horizontal: 'left',
                      }}
                      open={Boolean(anchorElNav)}
                      onClose={handleCloseNavMenu}
                      sx={{
                          display: { xs: 'block', md: 'none' },
                      }}
                      >
                          {buttons.filter((item) => item.roles.includes(userRole))
                              .map((item, index) => {
                                      return (
                                          <ListItem key={"appbar-submenu-list-item-"+index} style={{padding: 2}} >
                                              <SubMenuItems key={"appbar-submenu-item-"+index} title={item.label} userRole={userRole} onSelection={() => {
                                                  //setCurrentButton(button)
                                                  handleCloseNavMenu()
                                                  }}/>
                                          </ListItem>
                                      )
                                  }
                          )}
                      </Menu>
                  </Box>
                  {/*Large menu*/}
                  <Box key={"navBar-top-buttons-box"} sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex'} }}>
                      <Grid key={"navBar-top-buttons-box-container"} container flexDirection={"row"} paddingTop={2}>
                      {/* Buttons on the left */}
                          <Grid key={"navBar-top-buttons-box-container-column"} container direction="column" padding={0}>
                              {/* First line of buttons */}
                              <Grid key={"navBar-top-buttons-box-container-first-line"} item container direction="row" justifyContent="center">
                                  {buttons.filter((item) => item.roles.includes(userRole))
                                      .map((item, index) => {
                                              const disabled = item === currentButton;
                                              return (
                                                      <Link key={"navBar-link-top-button-" + index} to={{ pathname: `/home${item.href}`}} style={{ textDecoration: 'none', paddingTop: 1, marginRight: 5 }}>
                                                         <Button key={"navBar-topbutton-" + index} disabled={disabled} style={{backgroundColor: disabled ? "#0B48CC" : "#5277FF"}}>
                                                          <Typography variant="body2" align="center" color={"white"}>{item.label}</Typography>
                                                         </Button>
                                                      </Link>
                                                  )
                                          }
                                      )}
                              </Grid>
                              {/* Second line of buttons */}
                              <Grid key={"navBar-top-buttons-box-container-second-line"} container justifyContent="center" marginBottom={2} marginTop={1}>
                                  {currentButton &&
                                      currentButton.subButtons
                                          .filter((subButton) => subButton.roles.includes(userRole))
                                          .map((subButton, index) => {
                                              const disabled = location.pathname === `/home${currentButton.href}${subButton.href}`
                                              return (
                                                  <Link
                                                    key={"navBar-link-bottom-button-" + index}
                                                    to={{ pathname: `/home${currentButton.href}${subButton.href}`}}
                                                    style={{ textDecoration: 'none', paddingTop: 1, marginRight: 5 }}>
                                                     <Button key={"navBar-link-bottom-button-" + index}  disabled={disabled} style={{backgroundColor: disabled ? "#0B48CC" : "#5277FF", border: "white", borderColor: "white"}}>
                                                      <Typography variant="body2" align="center" color={"white"}>{subButton.label}</Typography>
                                                     </Button>
                                                  </Link>
                                              )
                                          })}
                              </Grid>
                          </Grid>
                      </Grid>
                  </Box>
                  <Box>
                      {/* Profile box on the right */}
                      <Grid item>
                          <Grid direction="row" container alignItems="center" justifyContent="space-between">
                              <Grid item>
                                  <IconButton
                                      onClick={() => {
                                          fetch("/logout", {
                                              method: "POST"
                                          })
                                              .then((response) => {
                                                  if (response.ok) {
                                                      window.location.href = '/';
                                                  }
                                                  throw response;
                                              })
                                              .catch(error => {
                                                  console.error("ERROR ", error);
                                              });
                                      }}
                                      aria-label={"logout"}
                                      style={{fontSize: 'inherit'}}
                                  >
                                        <LogoutIcon style={{width: '1em',height: 'auto', color: 'black'}}/>
                                  </IconButton>
                              </Grid>
                              <Grid item>
                                  <Link style={{textDecoration: 'none', color: 'inherit'}} to="/home/myAccount">
                                      <Grid container direction="column" alignItems="center">
                                          <Grid item>
                                              <Typography
                                                  variant="body1">{user && user.firstName} {user && user.lastName}</Typography>
                                          </Grid>
                                          <Grid item>
                                              <Typography variant="body2">{apiRoleToFrench(userRole)}</Typography>
                                          </Grid>
                                      </Grid>
                                  </Link>
                              </Grid>
                          </Grid>
                      </Grid>
                  </Box>
              </Toolbar>
          </Container>
      </AppBar>
    );
  }

  function SubMenuItems(props: {title: string, userRole: string, onSelection: () => void}){
      const [open, setOpen] = React.useState(false);

      const handleClick = () => {
        setOpen(!open);
      };

      return (
          <List
          sx={{ width: '100%', maxWidth: 360/*, bgcolor: 'background.paper'*/ }}
          component="nav"
          aria-labelledby="nested-list-subheader"
          style={{padding: 0}}
          >
              <ListItemButton onClick={handleClick} style={{padding: 0, margin: 0}} disableGutters>
                  <Typography variant="h6">{props.title}</Typography>
                  {open ? <ExpandLess /> : <ExpandMore />}
              </ListItemButton>
              <Collapse in={open} timeout="auto" unmountOnExit style={{padding: 0, margin: 0, maxWidth: "true"}} >
                  <List component="div" style={{padding: 0, maxWidth: "true"}} >
                      {buttons.filter((item) => item.label === props.title)
                          .map((item) => {
                                  return item.subButtons.map((subButton) => {
                                      return (
                                              <Link
                                              key={"link-menu-page-redirect-" + item.label + "-" + subButton.label}
                                              to={{ pathname: `/home${item.href}${subButton.href}`}}
                                              style={{ textDecoration: 'none', color: 'black', maxWidth: "true"}}>
                                                      <ListItemButton
                                                      key={"link-button-menu-page-redirect-" + item.label + "-" + subButton.label}
                                                      style={{maxWidth: "true", height:"5vh", padding: 0, marginLeft: 5}}
                                                      onClick={ev => props.onSelection()}>
                                                          <Typography variant="body1">{subButton.label}</Typography>
                                                      </ListItemButton>
                                              </Link>

                                      )
                                  })
                              }
                      )}
                  </List>
              </Collapse>
          </List>
      )
  }

export { apiRoleToFrench as englishRoleToFrench };
