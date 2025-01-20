<template>
  <v-app theme="light">
    <Header />
    <v-main>
      <v-container
        class="d-flex flex-column justify-center align-items-center mt-5"
      >
        <div class="d-flex align-items-center">
          <h1 class="text-center mr-auto">Minhas Tarefas</h1>
          <v-btn color="blue" @click="openCreateModal">
            <span>Criar tarefa</span>
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </div>
        <TaskList :tasks="tasks" />
      </v-container>

      <ModalCreateTask v-model="isOpenModal" @save-task="createTask" />
    </v-main>
  </v-app>
</template>

<script>
import { ref } from "vue";
import axios from "axios";
import TaskList from "./components/TaskList.vue";
import ModalCreateTask from "./components/ModalCreateTask.vue";

export default {
  components: { TaskList, ModalCreateTask },

  setup() {
    const tasks = ref([]);
    const isOpenModal = ref(false);

    const fetchTasks = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/tasks");
        tasks.value = response.data;
      } catch (error) {
        console.error("Failed at get tasks", error);
      }
    };

    const openCreateModal = () => {
      isOpenModal.value = true;
    };

    const closeCreateModal = () => {
      isOpenModal.value = false;
    };

    const createTask = async (newTask) => {
      try {
        const response = await axios.post(
          "http://localhost:8080/api/v1/tasks",
          newTask
        );
        tasks.value.push(response.data);
        closeCreateModal();
      } catch (error) {
        console.error("Failed at create task", error);
      }
    };

    fetchTasks();

    return {
      tasks,
      isOpenModal,
      openCreateModal,
      createTask,
    };
  },
};
</script>
