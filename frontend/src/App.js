import React, { useState } from 'react';
import './App.css';

function App() {
  // States for user inputs
  const [totalTickets, setTotalTickets] = useState('');
  const [ticketReleaseRate, setTicketReleaseRate] = useState('');
  const [customerRetrievalRate, setCustomerRetrievalRate] = useState('');
  const [maxTicketCapacity, setMaxTicketCapacity] = useState('');
  const [numberOfVendors, setNumberOfVendors] = useState('');
  const [numberOfCustomers, setNumberOfCustomers] = useState('');

  // State to handle errors
  const [errors, setErrors] = useState('');

  // States to handle logs and system status
  const [logs, setLogs] = useState([]);
  const [isStarted, setIsStarted] = useState(false);

  // Validation function for inputs
  const validateInputs = () => {
    const inputs = [
      totalTickets,
      ticketReleaseRate,
      customerRetrievalRate,
      maxTicketCapacity,
      numberOfVendors,
      numberOfCustomers,
    ];

    for (let input of inputs) {
      if (isNaN(input) || input <= 0) {
        return false;
      }
    }

    if (parseInt(totalTickets) > parseInt(maxTicketCapacity)) {
      return false;
    }

    return true;
  };

  // Handle Submit Configuration
  const handleSubmit = async () => {
    if (validateInputs()) {
      setErrors('');
      const configData = {
        totalTickets,
        ticketReleaseRate,
        customerRetrievalRate,
        maxTicketCapacity,
        numberOfVendors,
        numberOfCustomers,
      };

      // Send the data to Spring Boot backend using fetch
      const response = await fetch('http://localhost:8080/api/configurations/add', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(configData), // Ensure this is a valid JSON object
      });;

      if (response.ok) {
        const data = await response.json();
        setLogs([...logs, `Configuration submitted: ${JSON.stringify(data)}`]);
      } else {
        setLogs([...logs, 'Error submitting configuration']);
      }
    } else {
      setErrors('Please enter valid positive integers, and ensure totalTickets <= maxTicketCapacity.');
    }
  };

  // Handle Start Button
  const handleStart = async () => {
    if (isStarted) return;
    // Send a request to start the simulator using fetch
    const response = await fetch('http://localhost:8080/api/simulator/start', {
      method: 'POST',
    });

    if (response.ok) {
      const data = await response.json();
      setIsStarted(true);
      setLogs([...logs, `Simulator started: ${JSON.stringify(data)}`]);
    } else {
      setLogs([...logs, 'Error starting the simulator']);
    }
  };

  // Handle Stop Button (Fetch logs from backend)
  const handleStop = async () => {
    const response = await fetch('http://localhost:8080/api/simulator/stop', {
      method: 'GET',
    });

    if (response.ok) {
      const data = await response.text(); // Assuming the response contains a string
      setIsStarted(false);
      setLogs([...logs, `Simulator stopped: ${data}`]);
    } else {
      setLogs([...logs, 'Error stopping the simulator']);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Ticketing System</h1>

        {/* Configuration Inputs */}
        <section>
          <h2>Configuration</h2>
          <label>
            Total Tickets:
            <input
              type="number"
              value={totalTickets}
              onChange={(e) => setTotalTickets(e.target.value)}
            />
          </label>
          <label>
            Ticket Release Rate:
            <input
              type="number"
              value={ticketReleaseRate}
              onChange={(e) => setTicketReleaseRate(e.target.value)}
            />
          </label>
          <label>
            Customer Retrieval Rate:
            <input
              type="number"
              value={customerRetrievalRate}
              onChange={(e) => setCustomerRetrievalRate(e.target.value)}
            />
          </label>
          <label>
            Max Ticket Capacity:
            <input
              type="number"
              value={maxTicketCapacity}
              onChange={(e) => setMaxTicketCapacity(e.target.value)}
            />
          </label>
          <label>
            Number of Vendors:
            <input
              type="number"
              value={numberOfVendors}
              onChange={(e) => setNumberOfVendors(e.target.value)}
            />
          </label>
          <label>
            Number of Customers:
            <input
              type="number"
              value={numberOfCustomers}
              onChange={(e) => setNumberOfCustomers(e.target.value)}
            />
          </label>

          {/* Display error message if there are any */}
          {errors && <p style={{ color: 'red' }}>{errors}</p>}
          <button onClick={handleSubmit}>Submit Configuration</button>
        </section>

        {/* Start/Stop Buttons */}
        <div>
          <button onClick={handleStart} disabled={isStarted}>Start</button>
          <button onClick={handleStop} disabled={isStarted}>Stop</button>
        </div>

        {/* Logs Section */}
        <section>
          <h2>Logs</h2>
          <ul>
            {logs.map((log, index) => (
              <li key={index}>{log}</li>
            ))}
          </ul>
        </section>
      </header>
    </div>
  );
}

export default App;
