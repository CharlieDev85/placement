package com.blue.ui;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Layout
public class BasicLayout extends AppLayout {

    public BasicLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("blue academy");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
    }

    private SideNav getSideNav() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem("Welcome", "/welcome", VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Student", "/student-form", VaadinIcon.CART.create()),
                new SideNavItem("Quiz", "/quiz", VaadinIcon.USER_HEART.create()),
                new SideNavItem("Admin", "/admin",VaadinIcon.BELL.create()),
                new SideNavItem("Attempts", "/attempts",VaadinIcon.GLASS.create()),
                new SideNavItem("Recommendations", "/recommendations", VaadinIcon.CALC_BOOK.create()));
        return sideNav;
    }

}
