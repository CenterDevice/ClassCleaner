# ClassCleaner

ClassCleaner is an eclipse plugin to help you split up classes and to reorganize class methods. Therefore, ClassCleaner analyzes references of methods in your class, to identify groups of methods that do not refer to each other. If such methods are detected, ClassCleaner suggests to extract these methods into a separate class in order to keep your classes small.

## Building

Checkout the project and run

    mvn eclipse:eclipse
    
To run the project, select

    Run As -> Eclipse Application
    
To enable ClassCleaner for your project, right-click your project in the package explorer and select

    Configure -> Enable ClassCleaner