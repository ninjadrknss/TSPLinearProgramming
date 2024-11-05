## Traveling Salesman Problem Attempted Through Linear Programming

## Notes:
- Designed for the solver located at https://online-optimizer.appspot.com/
- For US, use all Subtour Elimination Constraints but st16 to achieve near optimal tour
  - Include last constraint to create a continuous tour
- Only the US and South America regions have subtour constraints created already
- EdgePlot.java file adapted from _Mr. Paige_'s `Plot.java` file
- All files except for `Locations.java` in `TSP_Datasets` folder and all files in the `TSP_Tools` folder are written by _Mr. Paige_

### Instructions:
- `LinearProgramming.java` is used to generate the code for the solver
- Change the `loc` variable to change the current region from the enum 
  - NOTE: you have to change the same variable in `EdgePlot.java` too to avoid errors
- Take the code found in the standard output (or redirect output to a file) and paste that into the solver
- Once the model is solved in the solver, paste the contents of `Output` into the console below "# Paste Plot:" when running `LinearProgramming.java` or just run `EdgePlot.java` and then paste
- Now view the resulting graph and have fun!