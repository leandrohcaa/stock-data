import React, { useEffect, useState } from 'react';
import api from '../config/api';
import { Select, MenuItem, FormControl, InputLabel, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';

const Main = () => {
  const [weeks, setWeeks] = useState([]);
  const [selectedWeek, setSelectedWeek] = useState('');
  const [symbols, setSymbols] = useState([]);
  const [stockData, setStockData] = useState({});

  useEffect(() => {
    const initializeDatabaseAndCollectData = async () => {
      try {
        await api.createDatabase();
        await api.collectData({
          startDate: "2024-06-24",
          symbols: ["MSFT", "AAPL", "NVDA", "AMZN", "META", "GOOGL", "GOOG", "BRK.B", "LLY", "AVGO"]
        });
        fetchWeeks();
        fetchSymbols();
      } catch (error) {
        console.error('Error initializing database and collecting data:', error);
      }
    };

    initializeDatabaseAndCollectData();
  }, []);

  useEffect(() => {
    if (selectedWeek && symbols.length > 0) {
      fetchStockDataForWeek(selectedWeek);
    }
  }, [selectedWeek, symbols]);

  const fetchWeeks = async () => {
    try {
      const response = await api.getWeeks();
      setWeeks(response.data);
      setSelectedWeek(response.data[0]);
    } catch (error) {
      console.error('Error fetching weeks:', error);
    }
  };

  const fetchSymbols = async () => {
    try {
      const response = await api.getSymbols();
      setSymbols(response.data);
    } catch (error) {
      console.error('Error fetching symbols:', error);
    }
  };

  const fetchStockDataForWeek = async (week) => {
    const stockDataPromises = symbols.map(async (symbol) => {
      const daysOfWeek = getDaysOfWeek(week);
      const symbolDataPromises = daysOfWeek.map(async (day) => {
        try {
          const response = await api.getStockData(symbol, day);
          return response.data;
        } catch (error) {
          if (error.response && error.response.status === 404) {
            return { date: day, openPrice: 0, closePrice: 0, highPrice: 0, lowPrice: 0, volume: 0 };
          } else {
            throw error;
          }
        }
      });
      const symbolDataResponses = await Promise.all(symbolDataPromises);
      return { symbol, data: symbolDataResponses };
    });

    const allStockData = await Promise.all(stockDataPromises);
    const stockDataMap = allStockData.reduce((acc, { symbol, data }) => {
      acc[symbol] = data;
      return acc;
    }, {});

    setStockData(stockDataMap);
  };

  const getDaysOfWeek = (startOfWeek) => {
    const startDate = new Date(startOfWeek);
    return Array.from({ length: 5 }, (_, i) => {
      const date = new Date(startDate);
      date.setDate(startDate.getDate() + i);
      return date.toISOString().split('T')[0];
    });
  };

  const calculateAverages = (data) => {
    if (!data || data.length === 0) return { avgOpenPrice: 0, avgClosePrice: 0, avgHighPrice: 0, avgLowPrice: 0, avgVolume: 0 };
    const total = data.reduce(
      (acc, day) => ({
        openPrice: acc.openPrice + day.openPrice,
        closePrice: acc.closePrice + day.closePrice,
        highPrice: acc.highPrice + day.highPrice,
        lowPrice: acc.lowPrice + day.lowPrice,
        volume: acc.volume + day.volume,
      }),
      { openPrice: 0, closePrice: 0, highPrice: 0, lowPrice: 0, volume: 0 }
    );

    const count = data.length;
    return {
      avgOpenPrice: total.openPrice / count,
      avgClosePrice: total.closePrice / count,
      avgHighPrice: total.highPrice / count,
      avgLowPrice: total.lowPrice / count,
      avgVolume: total.volume / count,
    };
  };

  return (
    <div>
      <h1>Stock Data Results</h1>
      <FormControl fullWidth>
        <InputLabel htmlFor="weekSelector">Select Week</InputLabel>
        <Select
          id="weekSelector"
          value={selectedWeek}
          onChange={(e) => setSelectedWeek(e.target.value)}
        >
          {weeks.map((week, index) => (
            <MenuItem key={index} value={week}>
              {week}
            </MenuItem>
          ))}
        </Select>
      </FormControl>

      {symbols.map((symbol) => {
        const averages = calculateAverages(stockData[symbol]);
        return (
          <div key={symbol}>
            <h2>{symbol}</h2>
            <TableContainer component={Paper}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Average Open Price</TableCell>
                    <TableCell>Average Close Price</TableCell>
                    <TableCell>Average High Price</TableCell>
                    <TableCell>Average Low Price</TableCell>
                    <TableCell>Average Volume</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  <TableRow>
                    <TableCell>{averages.avgOpenPrice.toFixed(2)}</TableCell>
                    <TableCell>{averages.avgClosePrice.toFixed(2)}</TableCell>
                    <TableCell>{averages.avgHighPrice.toFixed(2)}</TableCell>
                    <TableCell>{averages.avgLowPrice.toFixed(2)}</TableCell>
                    <TableCell>{averages.avgVolume.toFixed(0)}</TableCell>
                  </TableRow>
                </TableBody>
              </Table>
            </TableContainer>
          </div>
        );
      })}
    </div>
  );
};

export default Main;