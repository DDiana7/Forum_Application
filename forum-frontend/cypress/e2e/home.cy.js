describe("Login page", () => {
  it("should display the login page", () => {
    cy.visit("/");

    cy.contains("Login").should("be.visible");
    cy.get('input[placeholder="Email"]').should("be.visible");
    cy.get('input[placeholder="Password"]').should("be.visible");
    cy.get("button").contains("Login").should("be.visible");
    cy.contains("Register now!").should("be.visible");
  });

  it("should navigate to register page", () => {
    cy.visit("/");

    cy.contains("Register now!").click();

    cy.contains("Register").should("be.visible");
  });

  it("should display the register form", () => {
    cy.visit("/register");

    cy.contains("Register").should("be.visible");
    cy.get('input[placeholder="Username"]').should("be.visible");
    cy.get('input[placeholder="Email"]').should("be.visible");
    cy.get('input[placeholder="Password"]').should("be.visible");
    cy.get("button").contains("Register").should("be.visible");
  });

  it("should allow typing in login form", () => {
    cy.visit("/");

    cy.get('input[placeholder="Email"]').type("test@mail.com");
    cy.get('input[placeholder="Password"]').type("password123");

    cy.get('input[placeholder="Email"]').should("have.value", "test@mail.com");
    cy.get('input[placeholder="Password"]').should("have.value", "password123");
  });

  it("should allow typing in register form", () => {
    cy.visit("/register");

    cy.get('input[placeholder="Username"]').type("teodora");
    cy.get('input[placeholder="Email"]').type("teodora@mail.com");
    cy.get('input[placeholder="Password"]').type("password123");

    cy.get('input[placeholder="Username"]').should("have.value", "teodora");
    cy.get('input[placeholder="Email"]').should("have.value", "teodora@mail.com");
    cy.get('input[placeholder="Password"]').should("have.value", "password123");
  });

  it("should open the main page", () => {
    cy.visit("/main");

    cy.url().should("include", "/main");
  });

  it("should display main page content", () => {
    cy.visit("/main");

    cy.get("body").should("be.visible");
    cy.get("body").invoke("text").should("not.be.empty");
  });

  it("should login successfully and redirect to main page", () => {
    cy.intercept("POST", "http://localhost:8080/users/login", {
      statusCode: 200,
      body: {
        id: 1,
        username: "teodora",
        email: "test@mail.com",
        password: "password123"
      }
    }).as("loginRequest");

    cy.visit("/");

    cy.get('input[placeholder="Email"]').type("test@mail.com");
    cy.get('input[placeholder="Password"]').type("password123");
    cy.get("button").contains("Login").click();

    cy.wait("@loginRequest");

    cy.url().should("include", "/main");

    cy.window().then((window) => {
      const user = JSON.parse(window.localStorage.getItem("user"));
      expect(user.email).to.equal("test@mail.com");
      expect(user.username).to.equal("teodora");
    });
  });

  it("should open profile page when user is logged in", () => {
    cy.window().then((window) => {
      window.localStorage.setItem(
        "user",
        JSON.stringify({
          id: 1,
          username: "teodora",
          email: "test@mail.com",
          password: "password123"
        })
      );
    });

    cy.visit("/profile");

    cy.url().should("include", "/profile");
    cy.get("body").should("be.visible");
    cy.get("body").invoke("text").should("not.be.empty");
  });
});
